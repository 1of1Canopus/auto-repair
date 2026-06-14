package com.housedevinci.autorepair.quote.adapter.out.persistence;

import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.QuoteEntity;
import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
public class QuoteRepositoryAdapter implements QuoteRepository {

    private final QuoteJpaRepository jpaRepository;
    private final EntityMapper mapper;

    public QuoteRepositoryAdapter(QuoteJpaRepository jpaRepository, EntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findByJobId(UUID jobId) {
        return jpaRepository.findByJobsId(jobId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findByCustomerEmail(String customerEmail) {
        return jpaRepository.findByCustomerEmail(customerEmail).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Quote save(Quote quote) {
        QuoteEntity saved = jpaRepository.save(mapper.toEntity(quote));
        return mapper.toDomain(saved);
    }
}
