package com.example.spring.core.web.mappers;

import com.example.spring.core.account.Account;
import com.example.spring.core.web.dto.account.AccountResponse;

public class AccountMapper {
    public AccountMapper() {}

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getOwner(),
                account.getBalance()
        );
    }
}
