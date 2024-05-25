package com.example.workflow.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteApiClientResponse {

    private String quoteId;
    private String travelerName;
    private String email;
    private String origin;
    private String destination;
    private Double totalExpenseQuote;

}
