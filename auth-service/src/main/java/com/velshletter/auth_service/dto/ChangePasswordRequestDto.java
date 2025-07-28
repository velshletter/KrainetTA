package com.velshletter.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto (

    @NotBlank
    String oldPassword,

    @NotBlank
    @Size(min = 6, max = 100)
    String newPassword
){}
