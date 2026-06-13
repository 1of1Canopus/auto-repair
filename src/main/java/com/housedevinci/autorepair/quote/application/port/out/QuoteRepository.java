package com.housedevinci.autorepair.quote.application.port.out;

import com.housedevinci.autorepair.quote.domain.Quote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository {

    List<Quote> findAll();

    Optional<Quote> findById(UUID id);

    Optional<Quote> findByJobId(UUID jobId);

    List<Quote> findByCustomerEmail(String customerEmail);

    Quote save(Quote quote);
}
