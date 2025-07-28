package com.velshletter.auth_service.dto;

public record RegisterRequestDto(
        String username,
        String email,
        String password,
        String firstName,
        String lastName

        ) {
}
