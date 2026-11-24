package com.example.spring.core;

import com.example.spring.core.Audit.AuditLogRepository;
import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountRepository;
import com.example.spring.core.transfer.TransferRequest;
import com.example.spring.core.transfer.TransferService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@SpringBootApplication
@ConfigurationPropertiesScan
@Component
public class Demo implements CommandLineRunner {
    private final AccountRepository accounts;
    private final AuditLogRepository audits;
    private final TransferService transfers;

    public Demo(AccountRepository accounts, AuditLogRepository audits, TransferService transfers) {
        this.accounts = accounts;
        this.audits = audits;
        this.transfers = transfers;
    }

    @Override
    public void run(String... args) {
        Account a = accounts.save(new Account("Ali", new BigDecimal("500")));
        Account b = accounts.save(new Account("Vali", new BigDecimal("100")));
        Account d = accounts.save(new Account("Error", new BigDecimal("100")));

        transfers.transfer(a.getId(), b.getId(), new BigDecimal("100"));
        print("после перевода 100", a, b);          // 400 и 200

        try {
            transfers.transfer(a.getId(), b.getId(), new BigDecimal("1000"));
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        print("после неудачного перевода", a, b);   // всё ещё 400 и 200

        audits.findAll().forEach(l -> System.out.println("AUDIT: " + l.getMessage()));

        Account c = accounts.save(new Account("Gani", new BigDecimal("30")));
        try {
            transfers.transferAll(b.getId(), new BigDecimal("50"));
        } catch (Exception e) {
            System.out.println("Ожидаемое падение transferAll: " + e.getMessage());
        }
        print("после неудачного transferAll", a, b, c);
        transfers.transfersAll(List.of(
                new TransferRequest(a.getId(), b.getId(), new BigDecimal("50")),
                new TransferRequest(b.getId(), c.getId(), new BigDecimal("50"))
//                new TransferRequest(c.getId(), d.getId(), new BigDecimal("1050"))

        ));
//        transfers.transferAll(b.getId(),new BigDecimal("50"));
//        print("после transferAll (50 на Vali)", a, b); // у Ali станет 350, у Vali — 300 (200 + 50 + 50), у Gani — 250

        audits.findAll().forEach(l -> System.out.println("AUDIT: " + l.getMessage()));
    }

    private void print(String title, Account a, Account b) {
        System.out.println(title + ": " + accounts.findById(a.getId()).get().getBalance()
                + " / " + accounts.findById(b.getId()).get().getBalance());

    }
    private void print(String title, Account a, Account b, Account c) {
        System.out.println(title + ": " + accounts.findById(a.getId()).get().getBalance()
                + " / " + accounts.findById(b.getId()).get().getBalance()
                + " / " + accounts.findById(c.getId()).get().getBalance());
    }
}