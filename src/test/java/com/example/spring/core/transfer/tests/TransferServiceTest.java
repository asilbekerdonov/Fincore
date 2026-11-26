package com.example.spring.core.transfer.tests;



import com.example.spring.core.account.Account;
import com.example.spring.core.account.AccountRepository;
import com.example.spring.core.audit.AuditService;
import com.example.spring.core.config.TransferProperties;
import com.example.spring.core.transfer.Status;
import com.example.spring.core.transfer.Transfer;
import com.example.spring.core.transfer.TransferRepository;
import com.example.spring.core.transfer.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accounts;

    @Mock
    private TransferRepository transfers;

    @Mock
    private AuditService auditService;

    private TransferProperties properties;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        properties = new TransferProperties(new BigDecimal("10000.00"), true);
        transferService = new TransferService(accounts, transfers, auditService, properties);
    }

    @Test
    void shouldTransferSuccessfullyAndUpdateBalances() {
        Account from = new Account( "Asilbek", new BigDecimal("500.00"));
        Account to = new Account( "Ali", new BigDecimal("200.00"));

        when(accounts.findByIdForUpdate(1L)).thenReturn(Optional.of(from));
        when(accounts.findByIdForUpdate(2L)).thenReturn(Optional.of(to));
        when(transfers.save(any(Transfer.class))).thenAnswer(inv -> inv.getArgument(0));

        Transfer result = transferService.transfer(1L, 2L, new BigDecimal("150.00"));

        assertEquals(Status.SUCCESSFUL, result.getStatus());
        assertEquals(new BigDecimal("350.00"), from.getBalance());
        assertEquals(new BigDecimal("350.00"), to.getBalance());
        verify(transfers).save(any(Transfer.class));
        verify(auditService, atLeastOnce()).log(anyString()); // сверь точное имя метода AuditService
    }

    @Test
    void shouldThrowWhenTransfersDisabled() {
        TransferProperties disabledProps = new TransferProperties(new BigDecimal("10000.00"), false);
        TransferService service = new TransferService(accounts, transfers, auditService, disabledProps);

        assertThrows(IllegalStateException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("50.00")));

        verifyNoInteractions(accounts, transfers, auditService);
    }

    @Test
    void shouldThrowWhenAmountExceedsMaxLimit() {
        assertThrows(IllegalStateException.class,
                () -> transferService.transfer(1L, 2L, new BigDecimal("20000.00")));

        verifyNoInteractions(accounts, transfers, auditService);
    }
}