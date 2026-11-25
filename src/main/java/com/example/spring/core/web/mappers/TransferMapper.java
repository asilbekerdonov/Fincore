package com.example.spring.core.web.mappers;

import com.example.spring.core.enums.Status;
import com.example.spring.core.web.requests.TransferRequest;
import com.example.spring.core.web.requests.TransferResponse;

public final class TransferMapper {

    private TransferMapper() {}

    public static TransferResponse toResponse(TransferRequest request) {
        return new TransferResponse(
                Status.SUCCESSFUL,
                request.fromAccountId(),
                request.toAccountId(),
                request.amount()

        );
    }
}
