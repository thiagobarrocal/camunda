package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "The origin is required")
    private String origin;

    @NotBlank(message = "The destination is required")
    private String destination;

    @NotBlank(message = "The departure date is required")
    private String departureDate;

    @NotBlank(message = "The return date is required")
    private String returnDate;



}
