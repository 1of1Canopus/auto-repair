package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateJobRequest(
        @NotBlank String jobDescription,
        @NotNull @Positive BigDecimal labourTime,
        @NotNull @Positive BigDecimal labourRate,
        @PositiveOrZero BigDecimal overriddenPrice,
        boolean customerAuthorized) {
}
