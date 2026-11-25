package com.example.spring.core.web.requests;

import com.example.spring.core.enums.Status;

import java.math.BigDecimal;

public record TransferResponse( Status status, Long fromAccountId, Long toAccountId, BigDecimal amount) {
}
