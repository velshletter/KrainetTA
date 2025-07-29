package com.velshletter.auth_service.controller;
import com.velshletter.auth_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalController {

    private final AdminService adminService;

    @GetMapping("/admin-emails")
    public List<String> getAdminEmails() {
        return adminService.getAdminEmails();
    }
}