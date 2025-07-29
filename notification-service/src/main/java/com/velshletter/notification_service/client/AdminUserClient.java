package com.velshletter.notification_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "auth-service", url = "${auth-service.url}")
public interface AdminUserClient {

    @GetMapping("/api/internal/admin-emails")
    List<String> getAdminEmails(@RequestHeader("Authorization") String token);
}

