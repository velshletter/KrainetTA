package com.velshletter.notification_service.exception;

import java.time.LocalDateTime;

public record ErrorDetails(
        LocalDateTime timestamp,
        String message,
        String details,
        String errorCode
) {}