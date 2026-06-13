package com.housedevinci.autorepair.quote.application;

import com.housedevinci.autorepair.quote.application.port.out.JobCodeGenerator;
import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.Part;
import com.housedevinci.autorepair.quote.domain.Quote;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class QuoteService {

    private final QuoteRepository quotes;
    private final JobCodeGenerator jobCodes;

    public QuoteService(QuoteRepository quotes, JobCodeGenerator jobCodes) {
        this.quotes = quotes;
        this.jobCodes = jobCodes;
    }

    public List<Quote> getAllQuotes() {
        return quotes.findAll();
    }

    public Quote getQuoteById(UUID id) {
        return quotes.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
    }

    public List<Quote> getQuotesByCustomerEmail(String customerEmail) {
        return quotes.findByCustomerEmail(customerEmail);
    }

    public Quote createQuote(String customerName, String customerEmail,
                             String vrm, String vehicleDescription, int mileage) {
        return quotes.save(
                Quote.create(customerName, customerEmail, vrm, vehicleDescription, mileage));
    }

    public Job addJob(UUID quoteId, String jobDescription,
                      BigDecimal labourTime, BigDecimal labourRate, boolean isCustomerAuthorized) {
        Quote quote = quotes.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException(quoteId));
        Job job = Job.create(jobCodes.generate(), jobDescription,
                labourTime, labourRate, isCustomerAuthorized);
        quotes.save(quote.addJob(job));
        return job;
    }

    public void addPart(UUID jobId, Part part) {
        Quote quote = quotes.findByJobId(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
        quotes.save(quote.addPartToJob(jobId, part));
    }
}
