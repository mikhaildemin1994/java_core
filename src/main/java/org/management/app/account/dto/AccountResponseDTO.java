package org.management.app.account.dto;

import org.management.app.util.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponseDTO(
        long id,
        AccountType type,
        String currency,
        BigDecimal balance,
        Long departmentId
) {}
