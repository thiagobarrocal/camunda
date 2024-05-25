package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponseDto {

    private String quoteId;
    private String travelerName;
    private String email;
    private String origin;
    private String destination;
    private Double totalExpenseQuote;

}
