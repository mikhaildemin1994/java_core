package org.management.app.global.security.contoller;

import lombok.RequiredArgsConstructor;
import org.management.app.global.security.dto.TokenResponseDTO;
import org.management.app.global.security.dto.UserAuthRequestDTO;
import org.management.app.global.security.service.JwtAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authManager;
    private final JwtAuthenticationService authenticationService;

    @PostMapping("/auth/login")
    ResponseEntity<TokenResponseDTO> login(@RequestBody UserAuthRequestDTO request) {
        var login = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = authManager.authenticate(login);
        String token = authenticationService.generateToken((UserDetails) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
