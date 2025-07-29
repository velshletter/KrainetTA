package com.velshletter.auth_service.dto;

import com.velshletter.auth_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AdminUserUpdateRequestDto(
        @Size(min = 2, max = 50)
        String username,

        @Email
        @Size(max = 100)
        String email,

        @Size(min = 2, max = 50)
        String firstName,

        @Size(min = 2, max = 50)
        String lastName,

        Role role
) {
}
