package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.adapter.in.web.dto.CreatePartRequest;
import com.housedevinci.autorepair.quote.adapter.in.web.dto.PartResponse;
import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.Part;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PartController {

    private final QuoteService quoteService;
    private final WebMapper mapper;

    public PartController(QuoteService quoteService, WebMapper mapper) {
        this.quoteService = quoteService;
        this.mapper = mapper;
    }

    @PostMapping("/jobs/{jobId}/parts")
    public ResponseEntity<PartResponse> addPart(@PathVariable UUID jobId,
                                                @Valid @RequestBody CreatePartRequest request) {
        Part part = request.toDomain();
        quoteService.addPart(jobId, part);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(part));
    }
}
