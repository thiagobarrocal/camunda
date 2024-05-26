package com.example.workflow.service.quote;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.controller.dto.QuoteResponseDto;
import com.example.workflow.model.Quote;
import com.example.workflow.parser.QuoteRequestMapper;
import com.example.workflow.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuoteExternalService {

    private QuoteRepository quoteRepository;

    @Autowired
    public QuoteExternalService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public QuoteResponseDto createQuote(QuoteRequestDto quoteRequestDTO) {

        return QuoteResponseDto.builder()
                    .quoteId(this.saveQuote(quoteRequestDTO))
                    .build();
    }
    private String getQuoteReferenceId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private String saveQuote(QuoteRequestDto quoteRequestDTO) {
        var quote = QuoteRequestMapper.INSTANCE.parseToEntity(quoteRequestDTO);
        quote.setQuoteReferenceId(this.getQuoteReferenceId());
        quoteRepository.save(quote);
        return quote.getQuoteReferenceId();
    }
}