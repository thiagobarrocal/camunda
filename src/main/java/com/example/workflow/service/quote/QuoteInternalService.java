package com.example.workflow.service.quote;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.parser.QuoteRequestMapper;
import com.example.workflow.repository.QuoteRepository;
import com.example.workflow.utils.Checkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuoteInternalService {

    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteInternalService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public void saveQuote(QuoteRequestDto quoteRequestDTO) {
        checkConstraints(quoteRequestDTO);

        var entity = QuoteRequestMapper.INSTANCE.parseToEntity(quoteRequestDTO);
        quoteRepository.save(entity);
    }

    private void checkConstraints(QuoteRequestDto quoteRequestDTO) {
        Checkers.mustNotBeNull(quoteRequestDTO, "The quote request must not be null");
        Checkers.mustNotBeBlank(quoteRequestDTO.getQuoteReferenceId(), "The quote reference id  is required");
        Checkers.mustNotBeBlank(quoteRequestDTO.getTravelerName(), "The name of the traveler is required");
        Checkers.mustNotBeBlank(quoteRequestDTO.getEmail(), "The email is required");
        Checkers.mustNotBeNull(quoteRequestDTO.getOrigin(), "The origin is required");
        Checkers.mustNotBeBlank(quoteRequestDTO.getDestination(), "The destination is required");
        Checkers.mustNotBeBlank(quoteRequestDTO.getDepartureDate(), "The departure date is required");
        Checkers.mustNotBeBlank(quoteRequestDTO.getReturnDate(), "The return date is required");
    }

    private String getQuoteReferenceId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}