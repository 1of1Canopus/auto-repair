package com.housedevinci.autorepair.quote.application;

import com.housedevinci.autorepair.quote.application.port.out.JobCodeGenerator;
import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.Part;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuoteService {

    private final QuoteRepository quotes;
    private final JobCodeGenerator jobCodes;

    public QuoteService(QuoteRepository quotes, JobCodeGenerator jobCodes) {
        this.quotes = quotes;
        this.jobCodes = jobCodes;
    }

    @Transactional(readOnly = true)
    public List<Quote> getAllQuotes() {
        return quotes.findAll();
    }

    @Transactional(readOnly = true)
    public Quote getQuoteById(UUID id) {
        return quotes.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Quote> getQuotesByCustomerEmail(String customerEmail) {
        return quotes.findByCustomerEmail(customerEmail);
    }

    @Transactional
    public Quote createQuote(UUID customerId, String customerName, String customerEmail,
                             String vrm, String vehicleDescription, int mileage) {
        return quotes.save(
                Quote.create(customerId, customerName, customerEmail, vrm, vehicleDescription, mileage));
    }

    @Transactional
    public Job addJob(UUID quoteId, String jobDescription, BigDecimal labourTime,
                      BigDecimal labourRate, boolean isCustomerAuthorized,
                      Optional<BigDecimal> overriddenPrice) {
        Quote quote = quotes.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException(quoteId));
        String jobCode = jobCodes.generate();
        Job job = overriddenPrice
                .map(price -> Job.createWithFixedPrice(jobCode, jobDescription,
                        labourTime, labourRate, price, isCustomerAuthorized))
                .orElseGet(() -> Job.create(jobCode, jobDescription,
                        labourTime, labourRate, isCustomerAuthorized));
        quotes.save(quote.addJob(job));
        return job;
    }

    @Transactional
    public void addPart(UUID jobId, Part part) {
        Quote quote = quotes.findByJobId(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
        quotes.save(quote.addPartToJob(jobId, part));
    }
}
