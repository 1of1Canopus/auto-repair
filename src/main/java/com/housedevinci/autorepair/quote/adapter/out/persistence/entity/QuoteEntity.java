package com.housedevinci.autorepair.quote.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quotes")
public class QuoteEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String vrm;

    @Column(name = "vehicle_description", nullable = false)
    private String vehicleDescription;

    @Column(nullable = false)
    private int mileage;

    @Column(name = "date_created", nullable = false, updatable = false)
    private Instant dateCreated;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobEntity> jobs = new ArrayList<>();

    protected QuoteEntity() {
    }

    public QuoteEntity(UUID id, UUID customerId, String customerName, String customerEmail, String vrm,
                       String vehicleDescription, int mileage, Instant dateCreated) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vrm = vrm;
        this.vehicleDescription = vehicleDescription;
        this.mileage = mileage;
        this.dateCreated = dateCreated;
    }

    public void addJob(JobEntity job) {
        job.setQuote(this);
        jobs.add(job);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getVrm() {
        return vrm;
    }

    public String getVehicleDescription() {
        return vehicleDescription;
    }

    public int getMileage() {
        return mileage;
    }

    public List<JobEntity> getJobs() {
        return jobs;
    }
}
