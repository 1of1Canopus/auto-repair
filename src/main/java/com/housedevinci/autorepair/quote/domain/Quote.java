package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.List;

public record Quote(List<Job> jobs) {

    public BigDecimal total() {
        return jobs.stream()
                .filter(Job::isCustomerAuthorized)
                .map(Job::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
