package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FluidPartTest {

    @Test
    void total_cost_converts_millilitres_to_litres() {
        // 500 ml of oil at 12.00 per litre = 0.5 L * 12.00 = 6.00
        var part = new FluidPart(500, new BigDecimal("12.00"));

        assertThat(part.totalCost()).isEqualByComparingTo("6.00");
    }

    @Test
    void total_cost_is_rounded_to_the_cent() {
        // 1500 ml at 9.99 per litre = 1.5 * 9.99 = 14.985 -> 14.99
        var part = new FluidPart(1500, new BigDecimal("9.99"));

        assertThat(part.totalCost()).isEqualByComparingTo("14.99");
    }
}
