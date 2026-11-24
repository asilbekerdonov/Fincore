package com.example.spring.core;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {


    private final AccountRepository accounts;
    private final AuditService audit;
    public TransferService(AccountRepository accounts, AuditService audit) {
        this.accounts = accounts;
        this.audit = audit;

    }
    @Transactional
    public void transfer(long fromId, long toId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Не хватает средств");
        }
        if (fromId == toId) {
            throw new IllegalStateException("Тот же аккоунт!");

        }
        audit.log("transfer started: " + fromId + " --> "  + toId);
        Account from = accounts.findById(fromId).orElseThrow(() -> new IllegalArgumentException("Account not found: " + fromId));
        Account to = accounts.findById(toId).orElseThrow(() -> new IllegalArgumentException("Account not found: " + toId));
        from.withdraw(amount);
        to.deposit(amount);

    }

    @Transactional
    public void transferAll(long toId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account toAccount = accounts.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found: " + toId));

        List<Account> allAccounts = accounts.findAll();
        List<Account> senders = allAccounts.stream()
                .filter(acc -> !acc.getId().equals(toId))
                .toList();
        if (senders.isEmpty()) {
            throw new IllegalStateException("No other accounts available for transfer");
        }
        for (Account sender : senders) {
            sender.withdraw(amount);
            toAccount.deposit(amount);
        }

    }

    @Transactional
    public void transfersAll(List<TransferRequest> transferRequests) {
        for (TransferRequest request : transferRequests) {
            transfer(request.fromId(), request.toId(), request.amount());
        }
    }


}
