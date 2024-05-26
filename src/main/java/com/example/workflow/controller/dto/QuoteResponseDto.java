package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteResponseDto {

    private String quoteId;
    private String travelerName;
    private String email;
    private String origin;
    private String destination;
    private Double totalExpenseQuote;

}
