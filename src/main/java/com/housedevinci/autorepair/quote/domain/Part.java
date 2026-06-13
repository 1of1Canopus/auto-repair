package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;

public sealed interface Part permits MechanicalPart, FluidPart {

    BigDecimal totalCost();
}
