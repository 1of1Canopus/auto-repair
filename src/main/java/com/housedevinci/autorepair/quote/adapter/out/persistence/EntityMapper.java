package com.housedevinci.autorepair.quote.adapter.out.persistence;

import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.JobEntity;
import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.PartEntity;
import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.PartType;
import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.QuoteEntity;
import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Part;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class EntityMapper {

    public QuoteEntity toEntity(Quote quote) {
        Instant now = Instant.now();
        QuoteEntity entity = new QuoteEntity(quote.id(), quote.customerId(), quote.customerName(),
                quote.customerEmail(), quote.vrm(), quote.vehicleDescription(), quote.mileage(), now);
        quote.jobs().forEach(job -> entity.addJob(toEntity(job, now)));
        return entity;
    }

    private JobEntity toEntity(Job job, Instant now) {
        JobEntity entity = new JobEntity(job.id(), job.jobCode(), job.jobDescription(),
                job.labourTime(), job.labourRate(), job.overriddenPrice().orElse(null),
                job.isCustomerAuthorized(), now);
        job.parts().forEach(part -> entity.addPart(toEntity(part, now)));
        return entity;
    }

    private PartEntity toEntity(Part part, Instant now) {
        PartType type = switch (part) {
            case MechanicalPart ignored -> PartType.MECHANICAL;
            case FluidPart ignored -> PartType.FLUID;
        };
        return new PartEntity(part.id(), type, part.partNumber(), part.partDescription(),
                part.quantity(), part.unitCost(), now);
    }

    public Quote toDomain(QuoteEntity entity) {
        List<Job> jobs = entity.getJobs().stream().map(this::toDomain).toList();
        return new Quote(entity.getId(), entity.getCustomerId(), entity.getCustomerName(),
                entity.getCustomerEmail(), entity.getVrm(), entity.getVehicleDescription(),
                entity.getMileage(), jobs);
    }

    private Job toDomain(JobEntity entity) {
        List<Part> parts = entity.getParts().stream().map(this::toDomain).toList();
        return new Job(entity.getId(), entity.getJobCode(), entity.getJobDescription(),
                entity.getLabourTime(), entity.getLabourRate(), parts,
                Optional.ofNullable(entity.getOverriddenPrice()), entity.isCustomerAuthorized());
    }

    private Part toDomain(PartEntity entity) {
        return switch (entity.getType()) {
            case MECHANICAL -> new MechanicalPart(entity.getId(), entity.getPartNumber(),
                    entity.getPartDescription(), entity.getQuantity(), entity.getUnitCost());
            case FLUID -> new FluidPart(entity.getId(), entity.getPartNumber(),
                    entity.getPartDescription(), entity.getQuantity(), entity.getUnitCost());
        };
    }
}
