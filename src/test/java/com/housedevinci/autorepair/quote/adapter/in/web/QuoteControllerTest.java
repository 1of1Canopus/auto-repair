package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.application.QuoteNotFoundException;
import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.FluidPart;
import com.housedevinci.autorepair.quote.domain.Job;
import com.housedevinci.autorepair.quote.domain.MechanicalPart;
import com.housedevinci.autorepair.quote.domain.Quote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuoteController.class)
@Import(WebMapper.class)
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void get_quote_by_id_returns_full_hierarchy_with_total() throws Exception {
        var mechanical = MechanicalPart.create("BRK-001", "Brake pad", 2, new BigDecimal("45.00"));
        var fluid = FluidPart.create("OIL-001", "Engine oil", 500, new BigDecimal("12.00"));
        var job = new Job(UUID.randomUUID(), "JOB000001", "Replace brakes",
                new BigDecimal("1.5"), new BigDecimal("60.00"), List.of(mechanical, fluid),
                Optional.empty(), true);
        var quote = new Quote(UUID.randomUUID(), "Alice", "alice@example.com",
                "AA11AAA", "Audi A3", 1000, List.of(job));
        when(quoteService.getQuoteById(quote.id())).thenReturn(quote);

        mockMvc.perform(get("/api/v1/quotes/{id}", quote.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.total").value(186.00))
                .andExpect(jsonPath("$.jobs", hasSize(1)))
                .andExpect(jsonPath("$.jobs[0].price").value(186.00))
                .andExpect(jsonPath("$.jobs[0].parts", hasSize(2)))
                .andExpect(jsonPath("$.jobs[0].parts[0].type").value("MECHANICAL"));
    }

    @Test
    void get_quote_by_id_returns_404_when_missing() throws Exception {
        var id = UUID.randomUUID();
        when(quoteService.getQuoteById(id)).thenThrow(new QuoteNotFoundException(id));

        mockMvc.perform(get("/api/v1/quotes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Quote " + id + " not found"));
    }

    @Test
    void get_quotes_filters_by_customer_email() throws Exception {
        var quote = new Quote(UUID.randomUUID(), "Alice", "alice@example.com",
                "AA11AAA", "Audi A3", 1000, List.of());
        when(quoteService.getQuotesByCustomerEmail("alice@example.com")).thenReturn(List.of(quote));

        mockMvc.perform(get("/api/v1/quotes").param("customerEmail", "alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerEmail").value("alice@example.com"));
    }

    @Test
    void create_quote_returns_201_with_location() throws Exception {
        var created = new Quote(UUID.randomUUID(), "Bob", "bob@example.com",
                "BB22BBB", "BMW 1", 2000, List.of());
        when(quoteService.createQuote("Bob", "bob@example.com", "BB22BBB", "BMW 1", 2000))
                .thenReturn(created);

        mockMvc.perform(post("/api/v1/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerName":"Bob","customerEmail":"bob@example.com",
                                 "vrm":"BB22BBB","vehicleDescription":"BMW 1","mileage":2000}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/quotes/" + created.id()))
                .andExpect(jsonPath("$.customerName").value("Bob"));
    }

    @Test
    void create_quote_returns_400_when_email_invalid() throws Exception {
        mockMvc.perform(post("/api/v1/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerName":"Bob","customerEmail":"not-an-email",
                                 "vrm":"BB22BBB","vehicleDescription":"BMW 1","mileage":2000}
                                """))
                .andExpect(status().isBadRequest());
    }
}
