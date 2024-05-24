package com.example.workflow.exception.handler;

import com.example.workflow.controller.dto.Erro;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@ControllerAdvice
@Log
public class ControllerHandlerExceptions {

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Erro handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            stringBuilder.append(String.format("%s: %s; ", fieldName, errorMessage));
        });
        return new Erro(stringBuilder.toString());
    }*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Erro handleConstraintValidationException(ConstraintViolationException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = StreamSupport.stream(error.getPropertyPath().spliterator(), false)
                .reduce((a, b) -> b)
                .get()
                .toString();
            String errorMessage = error.getMessage();
            stringBuilder.append(String.format("%s: %s; ", fieldName, errorMessage));
        });
        return new Erro(stringBuilder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Erro handleMissingParameterException(MissingServletRequestParameterException ex) {
        return new Erro(String.format("'%s' é um campo obrigatório e não pode ser nulo", ex.getParameterName()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseBody
    public Erro handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getAllValidationResults().forEach(parameterValidationResult ->
            parameterValidationResult.getResolvableErrors().forEach(messageSourceResolvable ->
                stringBuilder.append(String.format(
                    "%s: %s; ",
                    parameterValidationResult.getMethodParameter().getParameterName(),
                    messageSourceResolvable.getDefaultMessage()
                ))
            )
        );
        return new Erro(stringBuilder.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationExceptions(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
