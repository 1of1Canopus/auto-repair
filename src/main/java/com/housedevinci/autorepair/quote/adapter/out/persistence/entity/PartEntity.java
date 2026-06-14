package com.housedevinci.autorepair.quote.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "parts")
public class PartEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartType type;

    @Column(name = "part_number", nullable = false)
    private String partNumber;

    @Column(name = "part_description", nullable = false)
    private String partDescription;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "date_created", nullable = false, updatable = false)
    private Instant dateCreated;

    protected PartEntity() {
    }

    public PartEntity(UUID id, PartType type, String partNumber, String partDescription,
                      int quantity, BigDecimal unitCost, Instant dateCreated) {
        this.id = id;
        this.type = type;
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.dateCreated = dateCreated;
    }

    void setJob(JobEntity job) {
        this.job = job;
    }

    public UUID getId() {
        return id;
    }

    public PartType getType() {
        return type;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }
}
