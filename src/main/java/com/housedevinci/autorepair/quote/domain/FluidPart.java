package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record FluidPart(int quantity, BigDecimal unitCost) implements Part {

    @Override
    public BigDecimal totalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity))
                .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
    }
}
