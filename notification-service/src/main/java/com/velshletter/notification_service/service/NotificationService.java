package com.velshletter.notification_service.service;

import com.velshletter.base_domains.dto.UserEventDto;

public interface NotificationService {
    void processEvent(UserEventDto dto);
}
