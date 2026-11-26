package com.example.spring.core.web.dto;

import com.example.spring.core.transfer.Status;

import java.math.BigDecimal;

public record TransferResponse( Status status, Long fromAccountId, Long toAccountId, BigDecimal amount) {
}
