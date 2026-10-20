package org.management.app.user.dto;

import org.management.app.util.enums.UserRole;

public record UserResponseDTO(
        Long id,
        UserRole userRole,
        String email,
        Long departmentId
) {}
