package com.example.spring.core.web.controllers;

import com.example.spring.core.transfer.Transfer;
import com.example.spring.core.transfer.TransferService;
import com.example.spring.core.web.mappers.TransferMapper;
import com.example.spring.core.web.dto.transfer.TransferRequest;
import com.example.spring.core.web.dto.transfer.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/transfers")
@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        Transfer transfer =  transferService.transfer(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount()
        );
        TransferResponse response = TransferMapper.toResponse(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.getById(id);
        return ResponseEntity.ok(TransferMapper.toResponse(transfer));
    }
}
