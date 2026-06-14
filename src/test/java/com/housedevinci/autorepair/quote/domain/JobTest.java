package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobTest {

    @Test
    void price_is_labour_time_times_rate() {
        var job = JobFixtures.job(new BigDecimal("1.5"), new BigDecimal("60.00"), List.of(), true);

        assertThat(job.price()).isEqualByComparingTo("90.00");
    }

    @Test
    void price_adds_part_costs_to_labour() {
        List<Part> parts = List.of(
                PartFixtures.mechanical(2, new BigDecimal("45.00")),
                PartFixtures.fluid(500, new BigDecimal("12.00"))
        );
        var job = JobFixtures.job(new BigDecimal("1.0"), new BigDecimal("60.00"), parts, true);

        assertThat(job.price()).isEqualByComparingTo("156.00");
    }

    @Test
    void price_is_rounded_to_the_cent() {
        var job = JobFixtures.job(new BigDecimal("1.25"), new BigDecimal("55.50"), List.of(), true);

        assertThat(job.price()).isEqualByComparingTo("69.38");
    }

    @Test
    void overridden_price_ignores_labour_and_parts() {
        List<Part> parts = List.of(PartFixtures.mechanical(10, new BigDecimal("99.00")));
        var job = JobFixtures.jobWithFixedPrice(
                new BigDecimal("5"), new BigDecimal("80.00"), parts, new BigDecimal("250.00"), true);

        assertThat(job.price()).isEqualByComparingTo("250.00");
    }
}
