package com.velshletter.auth_service.service.impl;

import com.velshletter.auth_service.kafka.UserKafkaProducer;
import com.velshletter.auth_service.model.Role;
import com.velshletter.auth_service.model.User;
import com.velshletter.auth_service.service.UserNotificationService;
import com.velshletter.base_domains.dto.ActionType;
import com.velshletter.base_domains.dto.UserEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {

    private final UserKafkaProducer producer;

    public void notifyIfRegularUser(User user, ActionType eventType, String rawPassword) {
        if (user.getRole() != Role.USER) {
            return;
        }

        producer.sendUserEvent(UserEventDto.builder()
                .actionType(eventType)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(rawPassword)
                .build());
    }
}
