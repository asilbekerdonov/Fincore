package com.example.spring.core.account;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountService {

    private final AccountRepository accounts ;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;

    }

    @Transactional(readOnly = true)
    public Account getById(Long id) {
            return accounts.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
