package org.management.app.department.dto;

public record DepartmentResponseDTO(
        Long id,
        String name,
        String code,
        Long managerId
) {}
