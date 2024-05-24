package com.example.workflow.controller;

import com.example.workflow.controller.dto.Erro;
import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.StartProcessException;
import com.example.workflow.service.ProcessService;
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
    public ResponseEntity<?> startProccess(@Valid @RequestBody TravelRequestDTO travelRequestDTO) {
        processService.startProcess(travelRequestDTO);
        return ResponseEntity.ok().body("Process started successfully");
    }

    @ExceptionHandler(StartProcessException.class)
    public ResponseEntity<Erro> handlerFeignException(final StartProcessException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Erro(ex.getMessage()));
    }
}
