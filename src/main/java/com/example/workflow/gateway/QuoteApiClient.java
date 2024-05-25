package com.example.workflow.gateway;

import com.example.workflow.controller.dto.QuoteRequestDto;
import com.example.workflow.gateway.dto.QuoteApiClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "quoteApiClient")
public interface QuoteApiClient {

    @PostMapping("/v1/quote")
    QuoteApiClientResponse execute(
            @RequestBody QuoteRequestDto quoteRequestDto
            );
}
