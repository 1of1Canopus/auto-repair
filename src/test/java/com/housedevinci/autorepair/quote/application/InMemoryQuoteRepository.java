package com.housedevinci.autorepair.quote.application;

import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import com.housedevinci.autorepair.quote.domain.Quote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryQuoteRepository implements QuoteRepository {

    private final Map<UUID, Quote> store = new HashMap<>();

    @Override
    public List<Quote> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Quote> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Quote> findByJobId(UUID jobId) {
        return store.values().stream()
                .filter(quote -> quote.jobs().stream().anyMatch(job -> job.id().equals(jobId)))
                .findFirst();
    }

    @Override
    public List<Quote> findByCustomerEmail(String customerEmail) {
        return store.values().stream()
                .filter(quote -> quote.customerEmail().equals(customerEmail))
                .toList();
    }

    @Override
    public Quote save(Quote quote) {
        store.put(quote.id(), quote);
        return quote;
    }
}
