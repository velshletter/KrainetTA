package com.velshletter.auth_service.service;

import com.velshletter.auth_service.dto.ChangePasswordRequestDto;
import com.velshletter.auth_service.dto.UserResponseDto;
import com.velshletter.auth_service.dto.UserUpdateRequestDto;
import com.velshletter.auth_service.security.CustomUserDetails;

import java.util.UUID;

public interface UserService {

    UserResponseDto getCurrentUser(CustomUserDetails userDetails);

    UserResponseDto updateProfile(UUID userId, UserUpdateRequestDto updateDto);

    void changePassword(UUID userId, ChangePasswordRequestDto changePasswordDto);

    void deleteCurrentUser(UUID userId);

}
