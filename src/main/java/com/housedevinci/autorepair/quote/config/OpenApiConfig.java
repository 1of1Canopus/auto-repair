package com.housedevinci.autorepair.quote.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autoRepairOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Auto Repair Quote API")
                .version("v1")
                .description("RESTful API for managing vehicle repair quotes — quotes, jobs and parts."));
    }
}
