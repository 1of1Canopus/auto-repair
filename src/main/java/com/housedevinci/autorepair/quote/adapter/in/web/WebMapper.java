package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.adapter.in.web.dto.JobResponse;
import com.housedevinci.autorepair.quote.adapter.in.web.dto.PartResponse;
import com.housedevinci.autorepair.quote.adapter.in.web.dto.QuoteResponse;
import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Part;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebMapper {

    public QuoteResponse toResponse(Quote quote) {
        List<JobResponse> jobs = quote.jobs().stream().map(this::toResponse).toList();
        return new QuoteResponse(quote.id(), quote.customerId(), quote.customerName(),
                quote.customerEmail(), quote.vrm(), quote.vehicleDescription(), quote.mileage(),
                quote.total(), jobs);
    }

    public JobResponse toResponse(Job job) {
        List<PartResponse> parts = job.parts().stream().map(this::toResponse).toList();
        return new JobResponse(job.id(), job.jobCode(), job.jobDescription(),
                job.labourTime(), job.labourRate(), job.overriddenPrice().orElse(null),
                job.isCustomerAuthorized(), job.price(), parts);
    }

    public PartResponse toResponse(Part part) {
        String type = switch (part) {
            case MechanicalPart ignored -> "MECHANICAL";
            case FluidPart ignored -> "FLUID";
        };
        return new PartResponse(part.id(), type, part.partNumber(), part.partDescription(),
                part.quantity(), part.unitCost(), part.totalCost());
    }
}
