package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.application.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartController.class)
@Import(WebMapper.class)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void add_mechanical_part_returns_201_with_total_cost() throws Exception {
        mockMvc.perform(post("/api/v1/jobs/{jobId}/parts", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"MECHANICAL","partNumber":"BRK-001","partDescription":"Brake pad",
                                 "quantity":2,"unitCost":45.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("MECHANICAL"))
                .andExpect(jsonPath("$.totalCost").value(90.00));
    }

    @Test
    void add_fluid_part_returns_201_with_litre_conversion() throws Exception {
        mockMvc.perform(post("/api/v1/jobs/{jobId}/parts", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"FLUID","partNumber":"OIL-001","partDescription":"Engine oil",
                                 "quantity":500,"unitCost":12.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("FLUID"))
                .andExpect(jsonPath("$.totalCost").value(6.00));
    }
}
