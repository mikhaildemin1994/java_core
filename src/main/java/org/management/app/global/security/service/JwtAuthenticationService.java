package org.management.app.global.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

public interface JwtAuthenticationService {
    String extractUsername(String token);

    String generateToken(UserDetails userDetails);

    String generateToken(HashMap<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails user);
}
