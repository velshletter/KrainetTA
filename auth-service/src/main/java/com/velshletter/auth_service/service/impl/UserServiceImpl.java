package com.velshletter.auth_service.service.impl;

import com.velshletter.auth_service.dto.ChangePasswordRequestDto;
import com.velshletter.auth_service.dto.UserResponseDto;
import com.velshletter.auth_service.dto.UserUpdateRequestDto;
import com.velshletter.auth_service.dto.mapper.UserMapper;
import com.velshletter.auth_service.exception.ConflictException;
import com.velshletter.auth_service.exception.InvalidOldPasswordException;
import com.velshletter.auth_service.exception.UserNotFoundException;
import com.velshletter.auth_service.model.User;
import com.velshletter.auth_service.repository.UserRepository;
import com.velshletter.auth_service.security.CustomUserDetails;
import com.velshletter.auth_service.service.UserNotificationService;
import com.velshletter.auth_service.service.UserService;
import com.velshletter.base_domains.dto.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserNotificationService userNotificationService;


    @Override
    public UserResponseDto getCurrentUser(CustomUserDetails userDetails) {
        User user = userDetails.user();
        log.debug("Current user: {}", user.getEmail());
        return userMapper.toUserDto(user);
    }

    @Override
    public UserResponseDto updateProfile(UUID userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

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

        userRepository.save(user);

        userNotificationService.notifyIfRegularUser(user, ActionType.UPDATED, null);
        return userMapper.toUserDto(user);
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequestDto changePasswordDto) {
        User user = fetchUserOrThrow(userId);

        if (!passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
        userRepository.save(user);
    }


    @Override
    public void deleteCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userRepository.deleteById(userId);
        userNotificationService.notifyIfRegularUser(user, ActionType.DELETED, null);
    }

    private User fetchUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

}
