package com.velshletter.notification_service.service.impl;

import com.velshletter.notification_service.mail.MailProperties;
import com.velshletter.notification_service.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public void sendEmail(List<String> to, String subject, String content) {
        for (String recipient : to) {
            log.info("Sending email to {} with subject '{}'", recipient, subject);
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(recipient);
                helper.setFrom(mailProperties.getFrom());
                helper.setSubject(subject);
                helper.setText(content, false);
            } catch (Exception e) {
                throw new RuntimeException("Failed to prepare email for " + recipient, e);
            }
            mailSender.send(message);
            log.info("Successfully sent email to {} with subject '{}'", recipient, subject);
        }
    }
}
