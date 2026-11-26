package com.example.spring.core.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
@Validated
@ConfigurationProperties(prefix = "bank.transfer")

public record TransferProperties(
        @NotNull @Positive BigDecimal maxAmount,
        boolean enabled
) {
}
