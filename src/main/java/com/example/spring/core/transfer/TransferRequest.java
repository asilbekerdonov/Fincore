package com.example.spring.core.transfer;

import java.math.BigDecimal;

public record TransferRequest(long fromId, long toId, BigDecimal amount) {

}
