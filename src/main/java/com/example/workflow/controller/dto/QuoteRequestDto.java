package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteRequestDto {

    private String quoteReferenceId;

    @NotBlank(message = "The name of customer is required")
    private String travelerName;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is invalid")
    private String email;

    @NotNull(message = "The total amount is required")
    private Double totalExpenseQuote;
}
