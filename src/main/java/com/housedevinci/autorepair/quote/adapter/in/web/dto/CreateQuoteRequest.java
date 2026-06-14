package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateQuoteRequest(
        @NotNull UUID customerId,
        @NotBlank String customerName,
        @NotBlank @Email String customerEmail,
        @NotBlank String vrm,
        @NotBlank String vehicleDescription,
        @PositiveOrZero int mileage) {
}
