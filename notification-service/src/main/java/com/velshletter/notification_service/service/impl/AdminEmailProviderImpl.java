package com.velshletter.notification_service.service.impl;

import com.velshletter.notification_service.client.AdminUserClient;
import com.velshletter.notification_service.security.JwtServiceTokenProvider;
import com.velshletter.notification_service.service.AdminEmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEmailProviderImpl implements AdminEmailProvider {

    private final AdminUserClient adminUserClient;
    private final JwtServiceTokenProvider tokenProvider;

    @Override
    public List<String> getAllAdminEmails() {
        String token = "Bearer " + tokenProvider.getServiceToken();
        List<String> emails = adminUserClient.getAdminEmails(token);
        log.info("Fetched {} admin emails from auth-service", emails.size());
        return emails;
    }
}
