package com.example.workflow.step;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.gateway.QuoteApiClient;
import com.example.workflow.service.QuoteInternalService;
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
    private static final String CAMUNDA_VARIABLE_ORIGIN = "origin";
    private static final String CAMUNDA_VARIABLE_DESTINATION = "destination";
    private static final String CAMUNDA_VARIABLE_DEPARTURE_DATE = "departureDate";
    private static final String CAMUNDA_VARIABLE_RETURN_DATE = "returnDate";


    private QuoteApiClient quoteApiClient;

    private QuoteInternalService quoteInternalService;

    @Autowired
    public QuoteStep(QuoteApiClient quoteApiClient, QuoteInternalService quoteInternalService) {
        this.quoteApiClient = quoteApiClient;
        this.quoteInternalService = quoteInternalService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Calling external quote api");
        var quoteRequest = QuoteRequestDto
                .builder()
                .travelerName(delegateExecution.getVariable(CAMUNDA_VARIABLE_TRAVELER_NAME).toString())
                .email(delegateExecution.getVariable(CAMUNDA_VARIABLE_EMAIL).toString())
                .origin(delegateExecution.getVariable(CAMUNDA_VARIABLE_ORIGIN).toString())
                .destination(delegateExecution.getVariable(CAMUNDA_VARIABLE_DESTINATION).toString())
                .departureDate(delegateExecution.getVariable(CAMUNDA_VARIABLE_DEPARTURE_DATE).toString())
                .returnDate(delegateExecution.getVariable(CAMUNDA_VARIABLE_RETURN_DATE).toString())
                .build();
        try {
            var apiQuoteResponse = quoteApiClient.execute(quoteRequest);
            quoteRequest.setQuoteReferenceId(apiQuoteResponse.getQuoteId());
            quoteInternalService.saveQuote(quoteRequest);
            delegateExecution.setVariable("quoteId", apiQuoteResponse.getQuoteId());

        } catch (FeignException.FeignClientException e) {
            log.error("Error calling quote api", e);
            throw e;
        }
    }
}
