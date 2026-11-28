package com.example.spring.core.events;

import com.example.spring.core.audit.AuditService;
import com.example.spring.core.transfer.Transfer;
import com.example.spring.core.transfer.TransferService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.Instant;


public record TransferCompletedEvent(
        Long transferId,
        long fromAccountId,
        long toAccountId,
        BigDecimal amount,
        Instant completedAt
){

}
