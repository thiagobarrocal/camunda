package com.example.workflow.step;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.gateway.QuoteApiClient;
import com.example.workflow.service.TravelService;
import com.example.workflow.utils.Constants;
import com.example.workflow.utils.TravelStatusEnum;
import feign.FeignException;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Named(Constants.QUOTE_STEP_BEAN_NAME)
@Service
public class QuoteStep implements JavaDelegate {

    @Value("${ERROR_QUOTE_API}")
    public Boolean ERROR_IN_QUOTE_API;

    private final QuoteApiClient quoteApiClient;

    private final TravelService travelService;

    @Autowired
    public QuoteStep(QuoteApiClient quoteApiClient, TravelService travelService) {
        this.quoteApiClient = quoteApiClient;
        this.travelService = travelService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Calling external quote api");
        var quoteRequest = QuoteRequestDto
                .builder()
                .travelerName(delegateExecution.getVariable(Constants.CAMUNDA_VARIABLE_TRAVELER_NAME).toString())
                .email(delegateExecution.getVariable(Constants.CAMUNDA_VARIABLE_EMAIL).toString())
                .totalExpenseQuote(Double.valueOf(delegateExecution.getVariable(Constants.CAMUNDA_VARIABLE_AMOUNT).toString()))
                .build();

        if (ERROR_IN_QUOTE_API) {
            throw new BpmnError(Constants.QUOTE_API_ERROR_CODE, "Error calling quote api");
        }
        var apiQuoteResponse = quoteApiClient.execute(quoteRequest);
        this.updateQuoteReferenceIdInTravel(quoteRequest.getEmail(), apiQuoteResponse.getQuoteId());
        this.updateTravelStatusByEmail(quoteRequest.getEmail(), TravelStatusEnum.APPROVED);
        delegateExecution.setVariable(Constants.CAMUNDA_VARIABLE_QUOTE_REFERENCE_ID, apiQuoteResponse.getQuoteId());
    }

    private void updateQuoteReferenceIdInTravel(String email, String quoteId) {
        travelService.updateTravelQuoteIdByEmail(email, quoteId);
    }

    public void updateTravelStatusByEmail(String email, TravelStatusEnum status) {
        travelService.updateTravelStatusByEmail(email, status);
    }
}
