package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteTest {

    @Test
    void total_is_sum_of_job_prices() {
        var job1 = Job.of(new BigDecimal("1.0"), new BigDecimal("60.00"), List.of(), true);
        var job2 = Job.of(new BigDecimal("2.0"), new BigDecimal("50.00"), List.of(), true);
        var quote = new Quote(List.of(job1, job2));

        assertThat(quote.total()).isEqualByComparingTo("160.00");
    }

    @Test
    void total_excludes_unauthorized_jobs() {
        var authorized = Job.of(new BigDecimal("1.0"), new BigDecimal("60.00"), List.of(), true);
        var unauthorized = Job.of(new BigDecimal("2.0"), new BigDecimal("50.00"), List.of(), false);
        var quote = new Quote(List.of(authorized, unauthorized));

        assertThat(quote.total()).isEqualByComparingTo("60.00");
    }

    @Test
    void total_of_quote_with_no_jobs_is_zero() {
        var quote = new Quote(List.of());

        assertThat(quote.total()).isEqualByComparingTo("0.00");
    }
}
