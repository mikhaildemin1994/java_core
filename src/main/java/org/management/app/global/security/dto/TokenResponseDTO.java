package org.management.app.global.security.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken
) {
    public TokenResponseDTO(String accessToken) {
        this(accessToken, "");
    }
}
