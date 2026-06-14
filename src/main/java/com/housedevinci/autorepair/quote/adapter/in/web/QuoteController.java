package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.adapter.in.web.dto.CreateQuoteRequest;
import com.housedevinci.autorepair.quote.adapter.in.web.dto.QuoteResponse;
import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.Quote;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;
    private final WebMapper mapper;

    public QuoteController(QuoteService quoteService, WebMapper mapper) {
        this.quoteService = quoteService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<QuoteResponse> getAll(@RequestParam(required = false) String customerEmail) {
        List<Quote> quotes = customerEmail == null
                ? quoteService.getAllQuotes()
                : quoteService.getQuotesByCustomerEmail(customerEmail);
        return quotes.stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public QuoteResponse getById(@PathVariable UUID id) {
        return mapper.toResponse(quoteService.getQuoteById(id));
    }

    @PostMapping
    public ResponseEntity<QuoteResponse> create(@Valid @RequestBody CreateQuoteRequest request) {
        Quote quote = quoteService.createQuote(request.customerId(), request.customerName(),
                request.customerEmail(), request.vrm(), request.vehicleDescription(), request.mileage());
        return ResponseEntity
                .created(URI.create("/api/v1/quotes/" + quote.id()))
                .body(mapper.toResponse(quote));
    }
}
