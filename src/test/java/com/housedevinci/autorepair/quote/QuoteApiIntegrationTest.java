package com.housedevinci.autorepair.quote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuoteApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_quote_then_add_job_and_parts_then_read_back_the_total() throws Exception {
        String quoteBody = mockMvc.perform(post("/api/v1/quotes")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"customerId":"44444444-4444-4444-4444-444444444444","customerName":"Alice",
                                 "customerEmail":"alice@example.com","vrm":"AA11AAA",
                                 "vehicleDescription":"Audi A3","mileage":1000}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String quoteId = objectMapper.readTree(quoteBody).get("id").asText();

        String jobBody = mockMvc.perform(post("/api/v1/quotes/{quoteId}/jobs", quoteId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"jobDescription":"Replace brakes","labourTime":1.5,
                                 "labourRate":60.00,"customerAuthorized":true}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String jobId = objectMapper.readTree(jobBody).get("id").asText();

        mockMvc.perform(post("/api/v1/jobs/{jobId}/parts", jobId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"type":"MECHANICAL","partNumber":"BRK-001","partDescription":"Brake pad",
                                 "quantity":2,"unitCost":45.00}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/jobs/{jobId}/parts", jobId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"type":"FLUID","partNumber":"OIL-001","partDescription":"Engine oil",
                                 "quantity":500,"unitCost":12.00}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/quotes/{id}", quoteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.customerId").value("44444444-4444-4444-4444-444444444444"))
                .andExpect(jsonPath("$.jobs", hasSize(1)))
                .andExpect(jsonPath("$.jobs[0].parts", hasSize(2)))
                .andExpect(jsonPath("$.jobs[0].price").value(186.00))
                .andExpect(jsonPath("$.total").value(186.00));
    }
}
