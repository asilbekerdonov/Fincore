package com.example.spring.core.account;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity

@Table(name = "accounts")

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, precision = 19, scale = 2)
    private Long id;
    private String owner;
    private BigDecimal balance;

    protected Account() {}
    public Account(String owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }
    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }

}
