package com.example.spring.core.web.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
