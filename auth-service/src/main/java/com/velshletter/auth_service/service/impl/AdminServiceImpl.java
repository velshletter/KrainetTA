package com.velshletter.auth_service.service.impl;

import com.velshletter.auth_service.dto.AdminUserUpdateRequestDto;
import com.velshletter.auth_service.dto.UserResponseDto;
import com.velshletter.auth_service.dto.mapper.UserMapper;
import com.velshletter.auth_service.exception.ConflictException;
import com.velshletter.auth_service.exception.UserNotFoundException;
import com.velshletter.auth_service.model.Role;
import com.velshletter.auth_service.model.User;
import com.velshletter.auth_service.repository.UserRepository;
import com.velshletter.auth_service.service.AdminService;
import com.velshletter.auth_service.service.UserNotificationService;
import com.velshletter.base_domains.dto.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserNotificationService userNotificationService;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {
        User user = fetchUser(userId);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserResponseDto updateUser(UUID userId, AdminUserUpdateRequestDto dto) {
        User user = fetchUser(userId);

        Optional.ofNullable(dto.username()).ifPresent(username -> {
            if (userRepository.existsByUsernameAndIdNot(username, userId)) {
                throw new ConflictException("Username already taken");
            }
            user.setUsername(username);
        });

        Optional.ofNullable(dto.email()).ifPresent(email -> {
            if (userRepository.existsByEmailAndIdNot(email, userId)) {
                throw new ConflictException("Email already taken");
            }
            user.setEmail(email);
        });

        Optional.ofNullable(dto.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(dto.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(dto.role()).ifPresent(user::setRole);
        userRepository.save(user);

        userNotificationService.notifyIfRegularUser(user, ActionType.UPDATED, null);

        return userMapper.toUserDto(user);
    }

    @Override
    public List<String> getAdminEmails() {
        return userRepository.findAllByRole(Role.ADMIN)
                .stream()
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = fetchUser(userId);
        userNotificationService.notifyIfRegularUser(user, ActionType.DELETED, null);
        userRepository.deleteById(userId);
    }

    private User fetchUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
