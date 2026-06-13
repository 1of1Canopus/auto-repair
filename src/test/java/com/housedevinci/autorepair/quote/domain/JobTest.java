package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobTest {

    @Test
    void price_is_labour_time_times_rate() {
        var job = Job.of(new BigDecimal("1.5"), new BigDecimal("60.00"), List.of());

        assertThat(job.price()).isEqualByComparingTo("90.00");
    }

    @Test
    void price_adds_part_costs_to_labour() {
        List<Part> parts = List.of(
                new MechanicalPart(2, new BigDecimal("45.00")),
                new FluidPart(500, new BigDecimal("12.00"))
        );
        var job = Job.of(new BigDecimal("1.0"), new BigDecimal("60.00"), parts);

        assertThat(job.price()).isEqualByComparingTo("156.00");
    }

    @Test
    void overridden_price_ignores_labour_and_parts() {
        List<Part> parts = List.of(new MechanicalPart(10, new BigDecimal("99.00")));
        var job = Job.withFixedPrice(
                new BigDecimal("5"), new BigDecimal("80.00"), parts, new BigDecimal("250.00"));

        assertThat(job.price()).isEqualByComparingTo("250.00");
    }
}
