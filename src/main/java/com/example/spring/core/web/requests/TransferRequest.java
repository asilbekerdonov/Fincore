package com.example.spring.core.web.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(@NotNull Long fromAccountId,
                              @NotNull Long toAccountId,
                              @Positive @NotNull BigDecimal amount) {
}

