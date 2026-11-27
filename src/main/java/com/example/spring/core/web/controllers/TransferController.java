package com.example.spring.core.web.controllers;

import com.example.spring.core.transfer.Status;
import com.example.spring.core.transfer.Transfer;
import com.example.spring.core.transfer.TransferService;
import com.example.spring.core.web.mappers.TransferMapper;
import com.example.spring.core.web.dto.transfer.TransferRequest;
import com.example.spring.core.web.dto.transfer.TransferResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


//@Operation(summary = "Create transfer")
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transfer completed"),
        @ApiResponse(responseCode = "400", description = "Validation or request error",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Account or transfer not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Business rule conflict (limits, disabled transfers, insufficient funds)",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
})
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
    @Operation(summary = "Get transfer history for an account")
    @GetMapping("/account/{accountId}")
    public Page<TransferResponse> getAccountTransfers(
            @PathVariable Long accountId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) BigDecimal minAmount
    ) {
        return transferService.getHistory(accountId,  status, minAmount, pageable)
                .map(TransferMapper::toResponse);
    }
}
