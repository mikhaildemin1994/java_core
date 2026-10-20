package org.management.app.account.dto;

import jakarta.validation.constraints.NotBlank;
import org.management.app.util.enums.AccountType;

public record AccountCreateDTO(
        AccountType accountType,

        @NotBlank(message = "Не выбрана валюта!")
        String currency,

        Long departmentId
) {}
