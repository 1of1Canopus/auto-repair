package com.housedevinci.autorepair.quote.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id", nullable = false)
    private QuoteEntity quote;

    @Column(name = "job_code", nullable = false, unique = true)
    private String jobCode;

    @Column(name = "job_description", nullable = false)
    private String jobDescription;

    @Column(name = "labour_time", nullable = false)
    private BigDecimal labourTime;

    @Column(name = "labour_rate", nullable = false)
    private BigDecimal labourRate;

    @Column(name = "overridden_price")
    private BigDecimal overriddenPrice;

    @Column(name = "customer_authorized", nullable = false)
    private boolean customerAuthorized;

    @Column(name = "date_created", nullable = false, updatable = false)
    private Instant dateCreated;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartEntity> parts = new ArrayList<>();

    protected JobEntity() {
    }

    public JobEntity(UUID id, String jobCode, String jobDescription, BigDecimal labourTime,
                     BigDecimal labourRate, BigDecimal overriddenPrice, boolean customerAuthorized,
                     Instant dateCreated) {
        this.id = id;
        this.jobCode = jobCode;
        this.jobDescription = jobDescription;
        this.labourTime = labourTime;
        this.labourRate = labourRate;
        this.overriddenPrice = overriddenPrice;
        this.customerAuthorized = customerAuthorized;
        this.dateCreated = dateCreated;
    }

    void setQuote(QuoteEntity quote) {
        this.quote = quote;
    }

    public void addPart(PartEntity part) {
        part.setJob(this);
        parts.add(part);
    }

    public UUID getId() {
        return id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public BigDecimal getLabourTime() {
        return labourTime;
    }

    public BigDecimal getLabourRate() {
        return labourRate;
    }

    public BigDecimal getOverriddenPrice() {
        return overriddenPrice;
    }

    public boolean isCustomerAuthorized() {
        return customerAuthorized;
    }

    public List<PartEntity> getParts() {
        return parts;
    }
}
