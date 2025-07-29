package com.velshletter.auth_service.service;

import com.velshletter.auth_service.dto.AdminUserUpdateRequestDto;
import com.velshletter.auth_service.dto.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(UUID userId);

    UserResponseDto updateUser(UUID userId, AdminUserUpdateRequestDto dto);

    void deleteUser(UUID userId);

    List<String> getAdminEmails();

}
