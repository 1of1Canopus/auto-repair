package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteTest {

    @Test
    void total_is_sum_of_job_prices() {
        var job1 = JobFixtures.job(new BigDecimal("1.0"), new BigDecimal("60.00"), List.of(), true);
        var job2 = JobFixtures.job(new BigDecimal("2.0"), new BigDecimal("50.00"), List.of(), true);

        assertThat(quoteWith(job1, job2).total()).isEqualByComparingTo("160.00");
    }

    @Test
    void total_excludes_unauthorized_jobs() {
        var authorized = JobFixtures.job(new BigDecimal("1.0"), new BigDecimal("60.00"), List.of(), true);
        var unauthorized = JobFixtures.job(new BigDecimal("2.0"), new BigDecimal("50.00"), List.of(), false);

        assertThat(quoteWith(authorized, unauthorized).total()).isEqualByComparingTo("60.00");
    }

    @Test
    void total_of_quote_with_no_jobs_is_zero() {
        assertThat(quoteWith().total()).isEqualByComparingTo("0.00");
    }

    private static Quote quoteWith(Job... jobs) {
        return new Quote(UUID.randomUUID(), UUID.randomUUID(), "Test Customer", "test@example.com",
                "AB12CDE", "Test Vehicle", 0, List.of(jobs));
    }
}
