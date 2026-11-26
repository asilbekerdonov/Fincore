package com.example.spring.core.web.dto.transfer;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
                                Long id,
                                @NotNull Long fromAccountId,
                              @NotNull Long toAccountId,
                              @Positive @NotNull BigDecimal amount) {
}

