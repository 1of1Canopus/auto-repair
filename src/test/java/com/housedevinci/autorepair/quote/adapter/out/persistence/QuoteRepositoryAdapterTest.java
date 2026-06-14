package com.housedevinci.autorepair.quote.adapter.out.persistence;

import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuoteRepositoryAdapterTest {

    @Autowired
    private QuoteRepository quotes;

    @Test
    void saves_and_reloads_a_quote_with_jobs_and_parts() {
        var mechanical = MechanicalPart.create("BRK-001", "Brake pad", 2, new BigDecimal("45.00"));
        var fluid = FluidPart.create("OIL-001", "Engine oil", 500, new BigDecimal("12.00"));
        var job = new Job(UUID.randomUUID(), "JOB000001", "Replace brakes",
                new BigDecimal("1.5"), new BigDecimal("60.00"), List.of(mechanical, fluid),
                Optional.empty(), true);
        var quote = new Quote(UUID.randomUUID(), "Alice", "alice@example.com",
                "AA11AAA", "Audi A3", 1000, List.of(job));

        quotes.save(quote);

        var reloaded = quotes.findById(quote.id()).orElseThrow();
        assertThat(reloaded.customerName()).isEqualTo("Alice");
        assertThat(reloaded.jobs()).hasSize(1);
        assertThat(reloaded.jobs().get(0).jobCode()).isEqualTo("JOB000001");
        assertThat(reloaded.jobs().get(0).parts()).hasSize(2);
        assertThat(reloaded.total()).isEqualByComparingTo("186.00");
    }
}
