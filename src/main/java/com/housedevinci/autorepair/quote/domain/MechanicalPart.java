package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record MechanicalPart(UUID id, String partNumber, String partDescription,
                             int quantity, BigDecimal unitCost) implements Part {

    public static MechanicalPart create(String partNumber, String partDescription,
                                        int quantity, BigDecimal unitCost) {
        return new MechanicalPart(UUID.randomUUID(), partNumber, partDescription, quantity, unitCost);
    }

    @Override
    public BigDecimal totalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
