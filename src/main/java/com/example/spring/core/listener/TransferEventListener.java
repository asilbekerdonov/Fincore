package com.example.spring.core.listener;

import com.example.spring.core.audit.AuditService;
import com.example.spring.core.events.TransferCompletedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Component
public class TransferEventListener {
    private final AuditService audit;

    public TransferEventListener(AuditService audit) {
        this.audit = audit;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTransferCompleted(TransferCompletedEvent event) {
        System.out.printf(
                "Успешный перевод #%d на сумму %s со счета %d на счет %d%n",
                event.transferId(),
                event.amount(),
                event.fromAccountId(),
                event.toAccountId()
        );
        audit.log("Transfer completed: " + event.transferId());

    }
}
