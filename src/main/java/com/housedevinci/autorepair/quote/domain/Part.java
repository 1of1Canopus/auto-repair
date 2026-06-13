package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface Part permits MechanicalPart, FluidPart {

    UUID id();

    String partNumber();

    String partDescription();

    int quantity();

    BigDecimal unitCost();

    BigDecimal totalCost();
}
