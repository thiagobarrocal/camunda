package com.example.workflow.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelProcessDto {

    @NotBlank(message = "The process id is required")
    private String processId;
}
