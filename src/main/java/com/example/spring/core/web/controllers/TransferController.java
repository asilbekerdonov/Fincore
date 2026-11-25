package com.example.spring.core.web.controllers;

import com.example.spring.core.transfer.TransferService;
import com.example.spring.core.web.mappers.TransferMapper;
import com.example.spring.core.web.requests.TransferRequest;
import com.example.spring.core.web.requests.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1/transfers")
@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }
    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        transferService.transfer(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount()
        );
        TransferResponse response = TransferMapper.toResponse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
