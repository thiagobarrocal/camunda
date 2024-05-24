package com.example.workflow.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TravelRequestDTO {

    //@NotBlank(message = "The name of customer is required")
    private String travelerName;

    //@NotBlank(message = "The email is required")
    //@Email(message = "The email is invalid")
    private String email;

    private String travelerDocument;

    //
    private String justification;
    //@NotBlank(message = "The origin is required")
    private String origin;
    //@NotBlank(message = "The destination is required")
    private String destination;

    //@NotNull(message = "The departure date is required")
    //@JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate departureDate;

    //@NotNull(message = "The return date is required")
    //@JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate returnDate;

    @NotNull(message = "The amount is required")
    private Double amount;

    @NotNull(message = "The department is required")
    private String department;

}
