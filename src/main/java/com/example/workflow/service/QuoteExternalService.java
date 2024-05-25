package com.example.workflow.service;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.controller.dto.QuoteResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuoteExternalService {
    public QuoteResponseDto createQuote(QuoteRequestDto quoteRequestDTO) {

        return QuoteResponseDto.builder()
                .quoteId(getQuoteReferenceId())
                .build();
    }
    private String getQuoteReferenceId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}