package com.example.spring.core.transfer;

import com.example.spring.core.audit.AuditService;
import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountRepository;
import com.example.spring.core.transfer.TransferRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {


    private final AccountRepository accounts;
    private final AuditService audit;
    private final TransferRepository transfers;
    public TransferService(AccountRepository accounts, AuditService audit, TransferRepository transfers) {
        this.accounts = accounts;
        this.audit = audit;
        this.transfers = transfers;

    }
    @Transactional
    public Transfer transfer(long fromId, long toId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Amount must be greater than zero   ");
        }
        if (fromId == toId) {
            throw new IllegalStateException("Cannot transfer to the same account");

        }

        audit.log("transfer started: " + fromId + " --> " + toId + ", amount=" + amount);
        Account from = accounts.findById(fromId).orElseThrow(() -> new IllegalArgumentException("Account not found: " + fromId));
        Account to = accounts.findById(toId).orElseThrow(() -> new IllegalArgumentException("Account not found: " + toId));
        from.withdraw(amount);
        to.deposit(amount);

        Transfer saved = transfers.save(
                new Transfer(fromId, toId, amount, Status.SUCCESSFUL)
        );
        audit.log("transfer completed: id=" + saved.getId());
        return saved;

    }



}
