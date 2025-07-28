package com.velshletter.auth_service.controller;

import com.velshletter.auth_service.dto.*;
import com.velshletter.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = service.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto token = service.authenticateAndGenerateToken(authRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestParam(name = "refreshToken") UUID refreshToken) {
        AuthResponseDto response = service.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@RequestParam(name = "refreshToken") UUID refreshToken) {
        service.revokeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
} 