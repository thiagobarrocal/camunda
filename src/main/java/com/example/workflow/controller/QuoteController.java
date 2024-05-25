package com.example.workflow.controller;

import com.example.workflow.controller.dto.Erro;
import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.exception.QuoteException;
import com.example.workflow.service.QuoteExternalService;
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
@RequestMapping(value = "/v1/quote")
public class QuoteController {

    private QuoteExternalService quoteService;

    public QuoteController(QuoteExternalService quoteService) {
        this.quoteService = quoteService;
    }

    @LogAround
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody QuoteRequestDto quoteRequestDto) {
        quoteService.createQuote(quoteRequestDto);
        return ResponseEntity.ok().body("Quote create successfully");
    }

    @ExceptionHandler(QuoteException.class)
    public ResponseEntity<Erro> handlerFeignException(final QuoteException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Erro(ex.getMessage()));
    }
}
