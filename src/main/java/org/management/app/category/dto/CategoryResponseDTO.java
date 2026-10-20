package org.management.app.category.dto;

public record CategoryResponseDTO(
        Long id,
        String name,
        Long parentCategoryId
) {}
