package com.example.spring.core.web.dto.account;

import java.math.BigDecimal;

public record AccountResponse(Long id, String owner, BigDecimal balance) {

}
