package org.management.app.counterparty.dto;

import org.management.app.util.enums.CounterpartyType;

public record CounterpartyCreateDTO(
        String name,
        String identityNumber,
        CounterpartyType type
) {}
