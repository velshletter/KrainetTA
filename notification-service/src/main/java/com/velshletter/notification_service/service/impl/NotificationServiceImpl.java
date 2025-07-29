package com.velshletter.notification_service.service.impl;

import com.velshletter.base_domains.dto.UserEventDto;
import com.velshletter.notification_service.service.AdminEmailProvider;
import com.velshletter.notification_service.service.MailService;
import com.velshletter.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MailService mailService;
    private final AdminEmailProvider adminEmailProvider;

    @Override
    public void processEvent(UserEventDto dto) {
        String subject = String.format("%s пользователь %s", dto.actionType(), dto.username());
        String message = String.format("""
                %s пользователь с:
                - именем: %s
                - почтой: %s
                - паролем: %s
                """,
                dto.actionType(), dto.username(), dto.email(), dto.password());

        List<String> adminEmails = adminEmailProvider.getAllAdminEmails();
        log.info("Preparing to notify {} admins about {} action for user {} (email: {})", adminEmails.size(), dto.actionType(), dto.username(), dto.email());
        mailService.sendEmail(adminEmails, subject, message);
        log.info("Notification emails sent for {} action for user {}", dto.actionType(), dto.username());
    }
}
