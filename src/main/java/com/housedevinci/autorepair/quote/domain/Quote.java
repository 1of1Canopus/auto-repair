package com.housedevinci.autorepair.quote.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Quote(UUID id, String customerName, String customerEmail,
                    String vrm, String vehicleDescription, int mileage, List<Job> jobs) {

    public static Quote create(String customerName, String customerEmail,
                               String vrm, String vehicleDescription, int mileage) {
        return new Quote(UUID.randomUUID(), customerName, customerEmail,
                vrm, vehicleDescription, mileage, List.of());
    }

    public Quote addJob(Job job) {
        var updatedJobs = new ArrayList<>(jobs);
        updatedJobs.add(job);
        return new Quote(id, customerName, customerEmail,
                vrm, vehicleDescription, mileage, List.copyOf(updatedJobs));
    }

    public Quote addPartToJob(UUID jobId, Part part) {
        var updatedJobs = jobs.stream()
                .map(job -> job.id().equals(jobId) ? job.withPart(part) : job)
                .toList();
        return new Quote(id, customerName, customerEmail,
                vrm, vehicleDescription, mileage, updatedJobs);
    }

    public BigDecimal total() {
        return jobs.stream()
                .filter(Job::isCustomerAuthorized)
                .map(Job::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
