package com.velshletter.auth_service.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.warn("User not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "USER_NOT_FOUND"
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.debug("Validation failed: {}", message);

        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        "Validation failed: " + message,
                        request.getDescription(false),
                        "VALIDATION_ERROR"
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtException(JwtException ex, WebRequest request) {
        log.warn("Invalid JWT: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false),
                        "INVALID_JWT"
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDetails> handleConflictException(ConflictException ex, WebRequest request) {
        log.info("Conflict detected: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false),
                        "CONFLICT_ERROR"
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String rootMessage = ex.getMostSpecificCause().getMessage();
        log.error("Data integrity violation: {}", rootMessage);

        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        "Database error: " + rootMessage,
                        request.getDescription(false),
                        "DATA_INTEGRITY_VIOLATION"
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidOldPasswordException.class)
    public ResponseEntity<ErrorDetails> handleInvalidOldPassword(InvalidOldPasswordException ex, WebRequest request) {
        log.warn("Invalid old password attempt: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false),
                        "INVALID_OLD_PASSWORD"
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD_CREDENTIALS"
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Unhandled exception at {}: {}", request.getDescription(false), ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorDetails(
                LocalDateTime.now(),
                "An unexpected error occurred.",
                request.getDescription(false),
                "INTERNAL_SERVER_ERROR"
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
