package com.velshletter.auth_service.service;

import com.velshletter.auth_service.dto.*;

import java.util.UUID;

public interface AuthService {
    RegisterResponseDto saveUser(RegisterRequestDto dto);
    AuthResponseDto authenticateAndGenerateToken(AuthRequestDto authRequestDto);
    AuthResponseDto refreshToken(UUID refreshToken);
    void revokeRefreshToken(UUID refreshToken);
    void validateToken(String token);
}