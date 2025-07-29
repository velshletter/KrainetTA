package com.velshletter.notification_service.service;

import java.util.List;

public interface MailService {
    void sendEmail(List<String> to, String subject, String content);
}
