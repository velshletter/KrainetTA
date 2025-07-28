package com.velshletter.auth_service.controller;

import com.velshletter.auth_service.dto.ChangePasswordRequestDto;
import com.velshletter.auth_service.dto.UserResponseDto;
import com.velshletter.auth_service.dto.UserUpdateRequestDto;
import com.velshletter.auth_service.security.CustomUserDetails;
import com.velshletter.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getCurrentUser(userDetails));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @Valid @RequestBody UserUpdateRequestDto updateDto) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.user().getId(), updateDto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @Valid @RequestBody ChangePasswordRequestDto changePasswordDto) {
        userService.changePassword(userDetails.user().getId(), changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteCurrentUser(userDetails.user().getId());
        return ResponseEntity.noContent().build();
    }

}