package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record Job(BigDecimal labourTime, BigDecimal labourRate, List<Part> parts,
                  Optional<BigDecimal> overriddenPrice) {

    public static Job of(BigDecimal labourTime, BigDecimal labourRate, List<Part> parts) {
        return new Job(labourTime, labourRate, parts, Optional.empty());
    }

    public static Job withFixedPrice(BigDecimal labourTime, BigDecimal labourRate,
                                     List<Part> parts, BigDecimal fixedPrice) {
        return new Job(labourTime, labourRate, parts, Optional.of(fixedPrice));
    }

    public BigDecimal price() {
        return overriddenPrice.orElseGet(this::calculatedPrice);
    }

    private BigDecimal calculatedPrice() {
        BigDecimal labour = labourTime.multiply(labourRate);
        BigDecimal partsCost = parts.stream()
                .map(Part::totalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return labour.add(partsCost);
    }
}
