package com.example.workflow.parser;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.controller.dto.QuoteResponseDto;
import com.example.workflow.model.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuoteRequestMapper {

    QuoteRequestMapper INSTANCE = Mappers.getMapper(QuoteRequestMapper.class);

    Quote parseToEntity(QuoteRequestDto quoteRequestDTO);
    QuoteResponseDto parseToDto(Quote quote);


}
