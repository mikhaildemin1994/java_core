package org.management.app.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
        @NotBlank(message = "Поле name не может быть пустым!")
        String name,
        Long parentCategoryId
) {}
