package com.housedevinci.autorepair.quote.config;

import com.housedevinci.autorepair.quote.application.QuoteService;
import com.housedevinci.autorepair.quote.application.port.out.JobCodeGenerator;
import com.housedevinci.autorepair.quote.application.port.out.QuoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuoteConfiguration {

    @Bean
    public QuoteService quoteService(QuoteRepository quoteRepository, JobCodeGenerator jobCodeGenerator) {
        return new QuoteService(quoteRepository, jobCodeGenerator);
    }
}
