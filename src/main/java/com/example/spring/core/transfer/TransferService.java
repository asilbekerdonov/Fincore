package com.example.spring.core.transfer;

import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountRepository;
import com.example.spring.core.audit.AuditService;


import com.example.spring.core.config.TransferProperties;
import com.example.spring.core.web.exception.BusinessRuleConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransferService {
    private final TransferProperties properties;
    private final AccountRepository accounts;
    private final TransferRepository transfers;
    private final AuditService audit;

    public TransferService(
            AccountRepository accounts,
            TransferRepository transfers,
            AuditService audit,
            TransferProperties properties

    ) {
        this.properties = properties;
        this.accounts = accounts;
        this.transfers = transfers;
        this.audit = audit;
    }

    @Transactional
    public Transfer transfer(long fromId, long toId, BigDecimal amount) {
        if (!properties.enabled()) {
            throw BusinessRuleConflictException.transfersDisabled();
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (properties.maxAmount() != null && amount.compareTo(properties.maxAmount()) > 0) {
            throw BusinessRuleConflictException.maxAmountExceeded(properties.maxAmount(), amount);
        }
        if (fromId == toId) {
            throw BusinessRuleConflictException.sameAccount(fromId);
        }
        audit.log("transfer started: " + fromId + " --> " + toId + ", amount=" + amount);

        long firstId = Math.min(fromId, toId);
        long secondId = Math.max(fromId, toId);

        Account firstAccount = accounts.findByIdForUpdate(firstId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + firstId));
        Account secondAccount = accounts.findByIdForUpdate(secondId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + secondId));

        Account from = (fromId == firstId) ? firstAccount : secondAccount;
        Account to = (toId == firstId) ? firstAccount : secondAccount;

        try {
            from.withdraw(amount);
        } catch (IllegalStateException ex) {
            throw BusinessRuleConflictException.insufficientFunds(
                    from.getId(), // подставь геттер ID счёта, если отличается
                    from.getBalance(), // подставь геттер баланса, если отличается
                    amount
            );
        }

        to.deposit(amount);

        Transfer saved = transfers.save(
                new Transfer(fromId, toId, amount, Status.SUCCESSFUL)
        );

        audit.log("transfer completed: id=" + saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Transfer getById(Long id) {


        return transfers.findById(id).orElseThrow(EntityNotFoundException::new) ;
    }
}