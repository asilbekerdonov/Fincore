package com.example.spring.core.web.controllers;

import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountService;
import com.example.spring.core.web.dto.account.AccountResponse;
import com.example.spring.core.web.mappers.AccountMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/accounts")
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(AccountMapper.toResponse(account));
    }
}
