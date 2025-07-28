package com.velshletter.auth_service.dto;

import java.util.UUID;

public record RegisterResponseDto(String message, UUID userId) { }