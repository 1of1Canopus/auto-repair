package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record Job(UUID id, String jobCode, String jobDescription,
                  BigDecimal labourTime, BigDecimal labourRate, List<Part> parts,
                  Optional<BigDecimal> overriddenPrice, boolean isCustomerAuthorized) {

    public static Job create(String jobCode, String jobDescription,
                             BigDecimal labourTime, BigDecimal labourRate,
                             boolean isCustomerAuthorized) {
        return new Job(UUID.randomUUID(), jobCode, jobDescription, labourTime, labourRate,
                List.of(), Optional.empty(), isCustomerAuthorized);
    }

    public static Job createWithFixedPrice(String jobCode, String jobDescription,
                                           BigDecimal labourTime, BigDecimal labourRate,
                                           BigDecimal fixedPrice, boolean isCustomerAuthorized) {
        return new Job(UUID.randomUUID(), jobCode, jobDescription, labourTime, labourRate,
                List.of(), Optional.of(fixedPrice), isCustomerAuthorized);
    }

    public Job withPart(Part part) {
        var updatedParts = new ArrayList<>(parts);
        updatedParts.add(part);
        return new Job(id, jobCode, jobDescription, labourTime, labourRate,
                List.copyOf(updatedParts), overriddenPrice, isCustomerAuthorized);
    }

    public BigDecimal price() {
        return overriddenPrice.orElseGet(this::calculatedPrice).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatedPrice() {
        BigDecimal labour = labourTime.multiply(labourRate);
        BigDecimal partsCost = parts.stream()
                .map(Part::totalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return labour.add(partsCost);
    }
}
