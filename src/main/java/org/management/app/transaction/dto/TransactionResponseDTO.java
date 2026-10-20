package org.management.app.transaction.dto;

import org.management.app.util.enums.TransactionStatus;
import org.management.app.util.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        TransactionType type,
        BigDecimal amount,
        String currency,
        LocalDateTime dateTime,
        Long accountFromId,
        Long accountToId,
        Long categoryId,
        Long counterpartyId,
        TransactionStatus status,
        Long userId
) {}
