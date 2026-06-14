package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMechanicalPartRequest(
        @NotBlank String partNumber,
        @NotBlank String partDescription,
        @Positive int quantity,
        @NotNull @Positive BigDecimal unitCost) implements CreatePartRequest {

    @Override
    public Part toDomain() {
        return MechanicalPart.create(partNumber, partDescription, quantity, unitCost);
    }
}
