package com.housedevinci.autorepair.quote.adapter.in.web;

import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.domain.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
@Import(WebMapper.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void add_job_returns_201_with_the_created_job() throws Exception {
        var quoteId = UUID.randomUUID();
        var job = Job.create("JOB000001", "Replace brakes",
                new BigDecimal("1.5"), new BigDecimal("60.00"), true);
        when(quoteService.addJob(any(UUID.class), anyString(), any(), any(), anyBoolean(), any()))
                .thenReturn(job);

        mockMvc.perform(post("/api/v1/quotes/{quoteId}/jobs", quoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jobDescription":"Replace brakes","labourTime":1.5,
                                 "labourRate":60.00,"customerAuthorized":true}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/quotes/" + quoteId))
                .andExpect(jsonPath("$.jobCode").value("JOB000001"))
                .andExpect(jsonPath("$.price").value(90.00));
    }

    @Test
    void add_job_returns_400_when_labour_time_missing() throws Exception {
        mockMvc.perform(post("/api/v1/quotes/{quoteId}/jobs", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jobDescription":"Replace brakes","labourRate":60.00,"customerAuthorized":true}
                                """))
                .andExpect(status().isBadRequest());
    }
}
