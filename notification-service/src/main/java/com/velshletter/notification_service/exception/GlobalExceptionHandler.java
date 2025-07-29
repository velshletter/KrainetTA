package com.velshletter.notification_service.exception;

import feign.FeignException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
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

    @ExceptionHandler({MailException.class, MessagingException.class})
    public ResponseEntity<ErrorDetails> handleMailException(Exception ex, WebRequest request) {
        log.error("Mail sending failed: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        "Failed to send email: " + ex.getMessage(),
                        request.getDescription(false),
                        "MAIL_ERROR"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignException(FeignException ex, WebRequest request) {
        log.error("Feign client error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        "Feign client error: " + ex.getMessage(),
                        request.getDescription(false),
                        "FEIGN_CLIENT_ERROR"
                ),
                HttpStatus.BAD_GATEWAY
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Unhandled exception", ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "An unexpected error occurred. Please contact support.",
                request.getDescription(false),
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}