package com.example.spring.core.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fincoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinCore API")
                        .version("v1")
                        .description("Financial transfers and accounts backend API with RFC 7807 ProblemDetail errors"));
    }
}