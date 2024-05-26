package com.example.workflow.step;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.gateway.QuoteApiClient;
import com.example.workflow.service.TravelService;
import com.example.workflow.utils.TravelStatusEnum;
import feign.FeignException;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Named("quoteStep")
@Service
public class QuoteStep implements JavaDelegate {

    private static final String CAMUNDA_VARIABLE_TRAVELER_NAME = "travelerName";
    private static final String CAMUNDA_VARIABLE_EMAIL = "email";
    private static final String CAMUNDA_VARIABLE_AMOUNT = "amount";

    private QuoteApiClient quoteApiClient;

    private TravelService travelService;

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
                .travelerName(delegateExecution.getVariable(CAMUNDA_VARIABLE_TRAVELER_NAME).toString())
                .email(delegateExecution.getVariable(CAMUNDA_VARIABLE_EMAIL).toString())
                .totalExpenseQuote(Double.valueOf(delegateExecution.getVariable(CAMUNDA_VARIABLE_AMOUNT).toString()))
                .build();
        try {
            var apiQuoteResponse = quoteApiClient.execute(quoteRequest);
            this.updateQuoteReferenceIdInTravel(quoteRequest.getEmail(), apiQuoteResponse.getQuoteId());
            this.updateTravelStatusByEmail(quoteRequest.getEmail(), TravelStatusEnum.APPROVED);
            delegateExecution.setVariable("quoteReferenceId", apiQuoteResponse.getQuoteId());

        } catch (FeignException.FeignClientException e) {
            log.error("Error calling quote api", e);
            throw e;
        }
    }

    private void updateQuoteReferenceIdInTravel(String email, String quoteId) {
        travelService.updateTravelQuoteIdByEmail(email, quoteId);
    }

    public void updateTravelStatusByEmail(String email, TravelStatusEnum status) {
        travelService.updateTravelStatusByEmail(email, status);
    }
}
