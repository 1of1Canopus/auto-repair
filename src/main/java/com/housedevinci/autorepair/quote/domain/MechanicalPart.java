package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;

public record MechanicalPart(int quantity, BigDecimal unitCost) {

    public BigDecimal totalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
