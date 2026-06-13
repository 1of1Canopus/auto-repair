package com.housedevinci.autorepair.quote.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MechanicalPartTest {

    @Test
    void total_cost_is_quantity_times_unit_cost() {
        var part = new MechanicalPart(2, new BigDecimal("45.00"));

        assertThat(part.totalCost()).isEqualByComparingTo("90.00");
    }
}
