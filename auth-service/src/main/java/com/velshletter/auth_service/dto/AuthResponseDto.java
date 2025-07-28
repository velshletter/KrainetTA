package com.velshletter.auth_service.dto;

import java.util.UUID;

public record AuthResponseDto(String accessToken, UUID refreshToken) {
}
