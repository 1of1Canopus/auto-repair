package com.housedevinci.autorepair.quote.config;

import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Profile("default")
public class SampleDataLoader implements ApplicationRunner {

    private final QuoteService quoteService;

    public SampleDataLoader(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public void run(ApplicationArguments args) {
        var brakes = quoteService.createQuote(
                "Alice Martin", "alice.martin@example.com", "AB12CDE", "Audi A3 1.6 TDI", 84000);
        var brakeJob = quoteService.addJob(brakes.id(), "Replace front brake pads and discs",
                new BigDecimal("1.5"), new BigDecimal("60.00"), true, Optional.empty());
        quoteService.addPart(brakeJob.id(),
                MechanicalPart.create("BRK-PAD-001", "Front brake pads", 1, new BigDecimal("45.00")));
        quoteService.addPart(brakeJob.id(),
                FluidPart.create("BRK-DOT4", "DOT4 brake fluid", 1000, new BigDecimal("9.50")));

        var service = quoteService.createQuote(
                "Bob Dupont", "bob.dupont@example.com", "XY98ZAB", "Renault Clio IV", 120000);
        quoteService.addJob(service.id(), "Major service (fixed price)",
                new BigDecimal("3"), new BigDecimal("70.00"), true, Optional.of(new BigDecimal("250.00")));
        quoteService.addJob(service.id(), "Replace cabin filter (awaiting authorisation)",
                new BigDecimal("0.5"), new BigDecimal("60.00"), false, Optional.empty());
    }
}
