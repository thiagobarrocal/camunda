package com.example.workflow.controller;

import com.example.workflow.controller.dto.CancelProcessDto;
import com.example.workflow.controller.dto.Erro;
import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.CamundaProcessException;
import com.example.workflow.service.ProcessService;
import im.aop.loggers.advice.around.LogAround;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/v1/proccess")
@ApiResponse(description = "Endpoints to manipulate Camunda Process", responseCode = "200")
public class ProccessController {

    private final ProcessService processService;

    public ProccessController(ProcessService processService) {
        this.processService = processService;
    }

    @LogAround
    @PostMapping(value = "/start")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Start the initial camunda process", description = "Start main BPMN process from application")
    public ResponseEntity<?> startProccess(@Valid @RequestBody TravelRequestDTO travelRequestDTO) {
        processService.startProcess(travelRequestDTO);
        return ResponseEntity.ok().body("Process started successfully");
    }

    @LogAround
    @PostMapping(value = "/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel the main camunda process", description = "Cancel the BPMN process from application")
    public ResponseEntity<?> cancelProcess(@Valid @RequestBody CancelProcessDto cancelProcessDto) {
        processService.cancelProcess(cancelProcessDto);
        return ResponseEntity.ok().body("Process cancelled successfully");
    }

    @ExceptionHandler(CamundaProcessException.class)
    public ResponseEntity<Erro> handlerFeignException(final CamundaProcessException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Erro(ex.getMessage()));
    }
}
