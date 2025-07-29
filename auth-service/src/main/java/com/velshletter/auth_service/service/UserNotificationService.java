package com.velshletter.auth_service.service;

import com.velshletter.auth_service.model.User;
import com.velshletter.base_domains.dto.ActionType;

public interface UserNotificationService {

    void notifyIfRegularUser(User user, ActionType eventType, String rawPassword);

}
