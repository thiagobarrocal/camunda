package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TravelRequestDTO {

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

    @NotNull(message = "The amount is required")
    private Double amount;

    @NotNull(message = "The department is required")
    private String department;

}
