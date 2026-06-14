package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record JobResponse(UUID id, String jobCode, String jobDescription,
                          BigDecimal labourTime, BigDecimal labourRate, BigDecimal overriddenPrice,
                          boolean customerAuthorized, BigDecimal price, List<PartResponse> parts) {
}
