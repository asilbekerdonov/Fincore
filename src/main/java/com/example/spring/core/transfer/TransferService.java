package com.example.spring.core.transfer;

import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountRepository;
import com.example.spring.core.audit.AuditService;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accounts;
    private final TransferRepository transfers;
    private final AuditService audit;

    public TransferService(
            AccountRepository accounts,
            TransferRepository transfers,
            AuditService audit
    ) {
        this.accounts = accounts;
        this.transfers = transfers;
        this.audit = audit;
    }

    @Transactional
    public Transfer transfer(long fromId, long toId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (fromId == toId) {
            throw new IllegalStateException("Cannot transfer to the same account");
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

        from.withdraw(amount);
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