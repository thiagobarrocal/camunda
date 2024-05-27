package com.example.workflow.controller;

import com.example.workflow.controller.dto.Erro;
import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.controller.dto.QuoteResponseDto;
import com.example.workflow.exception.QuoteException;
import com.example.workflow.service.quote.QuoteExternalService;
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
@RequestMapping(value = "/v1/quote")
@ApiResponse(description = "Endpoints to simulate external quote application", responseCode = "200")
public class QuoteController {

    private QuoteExternalService quoteService;

    public QuoteController(QuoteExternalService quoteService) {
        this.quoteService = quoteService;
    }

    @LogAround
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create the quote object in database", description = "This endpoint is used from [quote step] to simulate a external application that is used to storage a quote object")
    public ResponseEntity<QuoteResponseDto> create(@Valid @RequestBody QuoteRequestDto quoteRequestDto) {
        return ResponseEntity.ok().body(quoteService.createQuote(quoteRequestDto));
    }

    @ExceptionHandler(QuoteException.class)
    public ResponseEntity<Erro> handlerFeignException(final QuoteException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Erro(ex.getMessage()));
    }
}
