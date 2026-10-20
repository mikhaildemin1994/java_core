package org.management.app.transaction.dto;

import jakarta.validation.constraints.Positive;
import org.management.app.util.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionCreateDTO(
        TransactionType type,

        @Positive(message = "Must be positive")
        BigDecimal amount,

        String currency,
        Long accountFromId,
        Long accountToId,
        Long categoryId,
        Long counterpartyId,
        LocalDateTime date
) {
}
