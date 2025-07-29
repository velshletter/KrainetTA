package com.velshletter.auth_service.service.impl;

import com.velshletter.auth_service.dto.AuthRequestDto;
import com.velshletter.auth_service.dto.AuthResponseDto;
import com.velshletter.auth_service.dto.RegisterRequestDto;
import com.velshletter.auth_service.dto.RegisterResponseDto;
import com.velshletter.auth_service.model.RefreshToken;
import com.velshletter.auth_service.model.Role;
import com.velshletter.auth_service.model.User;
import com.velshletter.auth_service.repository.RefreshTokenRepository;
import com.velshletter.auth_service.repository.UserRepository;
import com.velshletter.auth_service.security.CustomUserDetails;
import com.velshletter.auth_service.security.JwtUtil;
import com.velshletter.auth_service.service.AuthService;
import com.velshletter.auth_service.service.UserNotificationService;
import com.velshletter.base_domains.dto.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserNotificationService userNotificationService;

    @Transactional
    public RegisterResponseDto saveUser(RegisterRequestDto dto) {

        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        userNotificationService.notifyIfRegularUser(user, ActionType.CREATED, dto.password());

        return new RegisterResponseDto("User registered successfully", savedUser.getId());
    }

    public AuthResponseDto authenticateAndGenerateToken(AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.email(),
                        authRequestDto.password()
                )
        );

        if (!authentication.isAuthenticated())
            throw new BadCredentialsException("Invalid email or password");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.user();
        String accessToken = jwtUtil.generateToken(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponseDto(accessToken, refreshToken.getId());
    }

    public AuthResponseDto refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = refreshTokenRepository.findByIdAndExpiresAtAfter(refreshToken, Instant.now())
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));

        final var newAccessToken = jwtUtil.generateToken(refreshTokenEntity.getUser());
        return new AuthResponseDto(newAccessToken, refreshToken);
    }

    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public void validateToken(String token) {
        jwtUtil.validateToken(token);
    }

}