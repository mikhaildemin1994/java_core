package org.management.app.counterparty.dto;

import org.management.app.util.enums.CounterpartyType;

public record CounterpartyResponseDTO(
        Long id,
        String name,
        String identityNumber,
        CounterpartyType type
) {}
