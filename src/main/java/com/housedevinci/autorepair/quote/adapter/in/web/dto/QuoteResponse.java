package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record QuoteResponse(UUID id, UUID customerId, String customerName, String customerEmail,
                            String vrm, String vehicleDescription, int mileage, BigDecimal total,
                            List<JobResponse> jobs) {
}
