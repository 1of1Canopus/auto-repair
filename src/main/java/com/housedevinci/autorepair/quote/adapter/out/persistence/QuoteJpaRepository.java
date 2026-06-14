package com.housedevinci.autorepair.quote.adapter.out.persistence;

import com.housedevinci.autorepair.quote.adapter.out.persistence.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, UUID> {

    List<QuoteEntity> findByCustomerEmail(String customerEmail);

    Optional<QuoteEntity> findByJobsId(UUID jobId);
}
