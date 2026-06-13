package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public record FluidPart(UUID id, String partNumber, String partDescription,
                        int quantity, BigDecimal unitCost) implements Part {

    public static FluidPart create(String partNumber, String partDescription,
                                   int quantity, BigDecimal unitCost) {
        return new FluidPart(UUID.randomUUID(), partNumber, partDescription, quantity, unitCost);
    }

    @Override
    public BigDecimal totalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity))
                .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
    }
}
