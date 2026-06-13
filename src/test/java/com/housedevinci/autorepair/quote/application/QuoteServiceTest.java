package com.housedevinci.autorepair.quote.application;

import com.housedevinci.autorepair.quote.application.port.out.JobCodeGenerator;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuoteServiceTest {

    private final InMemoryQuoteRepository quotes = new InMemoryQuoteRepository();
    private final JobCodeGenerator jobCodes = () -> "JOB000001";
    private final QuoteService service = new QuoteService(quotes, jobCodes);

    @Test
    void getQuoteById_throws_when_quote_not_found() {
        assertThatThrownBy(() -> service.getQuoteById(UUID.randomUUID()))
                .isInstanceOf(QuoteNotFoundException.class);
    }

    @Test
    void createQuote_stores_a_new_quote_with_no_jobs() {
        var created = service.createQuote(
                "Jane Doe", "jane@example.com", "AB12CDE", "Ford Focus", 45000);

        assertThat(created.id()).isNotNull();
        assertThat(created.jobs()).isEmpty();
        assertThat(quotes.findById(created.id())).contains(created);
    }

    @Test
    void getAllQuotes_returns_every_stored_quote() {
        service.createQuote("Alice", "alice@example.com", "AA11AAA", "Audi A3", 1000);
        service.createQuote("Bob", "bob@example.com", "BB22BBB", "BMW 1", 2000);

        assertThat(service.getAllQuotes()).hasSize(2);
    }

    @Test
    void getQuotesByCustomerEmail_returns_only_matching_quotes() {
        service.createQuote("Alice", "alice@example.com", "AA11AAA", "Audi A3", 1000);
        service.createQuote("Bob", "bob@example.com", "BB22BBB", "BMW 1", 2000);

        assertThat(service.getQuotesByCustomerEmail("alice@example.com"))
                .singleElement()
                .extracting(Quote::customerEmail)
                .isEqualTo("alice@example.com");
    }

    @Test
    void addJob_adds_a_generated_job_to_the_quote() {
        var quote = service.createQuote("Alice", "alice@example.com", "AA11AAA", "Audi A3", 1000);

        var job = service.addJob(quote.id(), "Replace brake pads",
                new BigDecimal("1.5"), new BigDecimal("60.00"), true);

        assertThat(job.id()).isNotNull();
        assertThat(job.jobCode()).isEqualTo("JOB000001");
        assertThat(service.getQuoteById(quote.id()).jobs()).contains(job);
    }

    @Test
    void addJob_throws_when_quote_not_found() {
        assertThatThrownBy(() -> service.addJob(UUID.randomUUID(), "x",
                BigDecimal.ONE, BigDecimal.TEN, true))
                .isInstanceOf(QuoteNotFoundException.class);
    }

    @Test
    void addPart_adds_a_part_to_the_job() {
        var quote = service.createQuote("Alice", "alice@example.com", "AA11AAA", "Audi A3", 1000);
        var job = service.addJob(quote.id(), "Replace brake pads",
                new BigDecimal("1.5"), new BigDecimal("60.00"), true);

        service.addPart(job.id(), new MechanicalPart(2, new BigDecimal("45.00")));

        assertThat(service.getQuoteById(quote.id()).jobs().get(0).parts()).hasSize(1);
    }

    @Test
    void addPart_throws_when_job_not_found() {
        assertThatThrownBy(() -> service.addPart(UUID.randomUUID(), new MechanicalPart(1, BigDecimal.TEN)))
                .isInstanceOf(JobNotFoundException.class);
    }
}
