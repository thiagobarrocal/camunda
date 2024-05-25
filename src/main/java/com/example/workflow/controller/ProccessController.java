package com.example.workflow.controller;

import com.example.workflow.controller.dto.CancelProcessDto;
import com.example.workflow.controller.dto.Erro;
import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.CamundaProcessException;
import com.example.workflow.service.ProcessService;
import im.aop.loggers.advice.around.LogAround;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/proccess")
public class ProccessController {

    private final ProcessService processService;

    public ProccessController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping(value = "/start")
    @LogAround
    public ResponseEntity<?> startProccess(@Valid @RequestBody TravelRequestDTO travelRequestDTO) {
        processService.startProcess(travelRequestDTO);
        return ResponseEntity.ok().body("Process started successfully");
    }

    @PostMapping(value = "/cancel")
    @LogAround
    public ResponseEntity<?> cancelProcess(@Valid @RequestBody CancelProcessDto cancelProcessDto) {
        processService.cancelProcess(cancelProcessDto);
        return ResponseEntity.ok().body("Process cancelled successfully");
    }

    @ExceptionHandler(CamundaProcessException.class)
    public ResponseEntity<Erro> handlerFeignException(final CamundaProcessException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Erro(ex.getMessage()));
    }
}
