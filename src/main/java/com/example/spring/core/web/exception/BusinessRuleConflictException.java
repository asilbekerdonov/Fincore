package com.example.spring.core.web.exception;



import java.math.BigDecimal;
import java.util.Map;

public class BusinessRuleConflictException extends RuntimeException {
    private final String typeSlug;
    private final String title;
    private final String errorCode;
    private final Map<String, Object> metadata;

    public BusinessRuleConflictException(
            String typeSlug,
            String title,
            String errorCode,
            String message,
            Map<String, Object> metadata
    ) {
        super(message);
        this.typeSlug = typeSlug;
        this.title = title;
        this.errorCode = errorCode;
        this.metadata = metadata;
    }

    public static BusinessRuleConflictException transfersDisabled() {
        return new BusinessRuleConflictException(
                "transfers-disabled",
                "Transfers Disabled",
                "TRANSFERS_DISABLED",
                "Transfers are currently disabled",
                Map.of()
        );
    }

    public static BusinessRuleConflictException insufficientFunds(
            long accountId,
            BigDecimal availableBalance,
            BigDecimal requestedAmount
    ) {
        return new BusinessRuleConflictException(
                "insufficient-funds",
                "Insufficient Funds",
                "INSUFFICIENT_FUNDS",
                "Insufficient funds on account: " + accountId,
                Map.of(
                        "accountId", accountId,
                        "availableBalance", availableBalance,
                        "requestedAmount", requestedAmount
                )
        );
    }
    public static BusinessRuleConflictException maxAmountExceeded(BigDecimal maxAmount, BigDecimal requestedAmount) {
        return new BusinessRuleConflictException(
                "limit-exceeded",
                "Limit Exceeded",
                "MAX_AMOUNT_EXCEEDED",
                "Transfer amount exceeds max limit: " + maxAmount,
                Map.of(
                        "maxAmount", maxAmount,
                        "requestedAmount", requestedAmount
                )
        );
    }

    public static BusinessRuleConflictException sameAccount(long accountId) {
        return new BusinessRuleConflictException(
                "same-account-transfer",
                "Same Account Transfer",
                "SAME_ACCOUNT_TRANSFER",
                "Cannot transfer to the same account",
                Map.of("accountId", accountId)
        );
    }

    public String getTypeSlug() { return typeSlug; }
    public String getTitle() { return title; }
    public String getErrorCode() { return errorCode; }
    public Map<String, Object> getMetadata() { return metadata; }
}