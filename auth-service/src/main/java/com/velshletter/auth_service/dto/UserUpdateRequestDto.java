package com.velshletter.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserUpdateRequestDto(
        @Size(min = 2, max = 50)
        String username,

        @Email
        @Size(max = 100)
        String email,

        @Size(min = 2, max = 50)
        String firstName,

        @Size(min = 2, max = 50)
        String lastName
) {
}
