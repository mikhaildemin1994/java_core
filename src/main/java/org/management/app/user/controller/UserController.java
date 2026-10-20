package org.management.app.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.management.app.global.security.dto.TokenResponseDTO;
import org.management.app.global.security.dto.UserAuthRequestDTO;
import org.management.app.user.dto.UserResponseDTO;
import org.management.app.user.servise.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> userAuthenticate(
            @Valid @RequestBody UserAuthRequestDTO authRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email(),
                        authRequest.password()
                )
        );

        String accessToken = "mock-access-token-for-" + authRequest.email();
        String refreshToken = "mock-refresh-token";

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userServiceImpl.getUserByEmail(userDetails.getUsername()));
    }
}
