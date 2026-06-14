package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PartResponse(UUID id, String type, String partNumber, String partDescription,
                           int quantity, BigDecimal unitCost, BigDecimal totalCost) {
}
