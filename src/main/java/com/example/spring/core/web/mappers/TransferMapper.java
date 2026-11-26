package com.example.spring.core.web.mappers;


import com.example.spring.core.transfer.Transfer;
import com.example.spring.core.web.dto.TransferResponse;

public final class TransferMapper {

    private TransferMapper() {}

    public static TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getStatus(),
                transfer.getFromAccountId(),
                transfer.getToAccountId(),
                transfer.getAmount()

        );
    }
}
