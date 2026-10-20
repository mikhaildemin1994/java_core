package org.management.app.department.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO(
        @NotBlank(message = "Поле name не может быть пустым!")
        String name,

        @NotBlank(message = "Поле code не может быть пустым!")
        String code,

        Long managerId
) {}
