package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateFluidPartRequest(
        @NotBlank String partNumber,
        @NotBlank String partDescription,
        @Positive int quantity,
        @NotNull @Positive BigDecimal unitCost) implements CreatePartRequest {

    @Override
    public Part toDomain() {
        return FluidPart.create(partNumber, partDescription, quantity, unitCost);
    }
}
