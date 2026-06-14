package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.adapter.in.web.dto.CreateJobRequest;
import com.housedevinci.autorepair.quote.adapter.in.web.dto.JobResponse;
import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.Job;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final QuoteService quoteService;
    private final WebMapper mapper;

    public JobController(QuoteService quoteService, WebMapper mapper) {
        this.quoteService = quoteService;
        this.mapper = mapper;
    }

    @PostMapping("/quotes/{quoteId}/jobs")
    public ResponseEntity<JobResponse> addJob(@PathVariable UUID quoteId,
                                              @Valid @RequestBody CreateJobRequest request) {
        Job job = quoteService.addJob(quoteId, request.jobDescription(), request.labourTime(),
                request.labourRate(), request.customerAuthorized(),
                Optional.ofNullable(request.overriddenPrice()));
        return ResponseEntity
                .created(URI.create("/api/v1/quotes/" + quoteId))
                .body(mapper.toResponse(job));
    }
}
