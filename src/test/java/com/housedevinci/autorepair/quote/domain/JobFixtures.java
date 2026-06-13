package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

final class JobFixtures {

    private JobFixtures() {
    }

    static Job job(BigDecimal labourTime, BigDecimal labourRate, List<Part> parts, boolean authorized) {
        return new Job(UUID.randomUUID(), "JOB000001", "Test job",
                labourTime, labourRate, parts, Optional.empty(), authorized);
    }

    static Job jobWithFixedPrice(BigDecimal labourTime, BigDecimal labourRate, List<Part> parts,
                                 BigDecimal fixedPrice, boolean authorized) {
        return new Job(UUID.randomUUID(), "JOB000001", "Test job",
                labourTime, labourRate, parts, Optional.of(fixedPrice), authorized);
    }
}
