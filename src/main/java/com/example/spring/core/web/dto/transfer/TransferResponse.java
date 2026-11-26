package com.example.spring.core.web.dto.transfer;

import com.example.spring.core.transfer.Status;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(Long id, Status status, Long fromAccountId, Long toAccountId, BigDecimal amount, Instant createdAt) {
}
