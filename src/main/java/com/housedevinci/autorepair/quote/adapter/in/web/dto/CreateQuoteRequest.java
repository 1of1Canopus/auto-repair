package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateQuoteRequest(
        @NotBlank String customerName,
        @NotBlank @Email String customerEmail,
        @NotBlank String vrm,
        @NotBlank String vehicleDescription,
        @PositiveOrZero int mileage) {
}
