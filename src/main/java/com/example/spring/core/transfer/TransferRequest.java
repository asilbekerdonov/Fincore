package com.example.spring.core.transfer;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "fromAccountId обязателен")
        Long fromAccountId,

        @NotNull(message = "toAccountId обязателен")
        Long toAccountId,

        @NotNull(message = "amount обязателен")
        @Positive(message = "amount должен быть больше 0")
        BigDecimal amount
) {
}