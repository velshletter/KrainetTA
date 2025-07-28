package com.velshletter.auth_service.dto;

import com.velshletter.auth_service.model.Role;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role
) {}
