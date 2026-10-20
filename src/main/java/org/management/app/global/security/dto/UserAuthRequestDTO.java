package org.management.app.global.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAuthRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
