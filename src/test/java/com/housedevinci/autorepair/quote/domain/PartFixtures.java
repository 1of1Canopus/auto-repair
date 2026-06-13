package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;

final class PartFixtures {

    private PartFixtures() {
    }

    static MechanicalPart mechanical(int quantity, BigDecimal unitCost) {
        return MechanicalPart.create("MECH-001", "Test mechanical part", quantity, unitCost);
    }

    static FluidPart fluid(int quantity, BigDecimal unitCost) {
        return FluidPart.create("FLUID-001", "Test fluid", quantity, unitCost);
    }
}
