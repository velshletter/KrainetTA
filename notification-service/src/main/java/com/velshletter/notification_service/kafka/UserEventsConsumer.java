package com.velshletter.notification_service.kafka;

import com.velshletter.base_domains.dto.UserEventDto;
import com.velshletter.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "user-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(UserEventDto eventDto) {
        log.info("Received user event: actionType={}, username={}, email={}",
                eventDto.actionType(), eventDto.username(), eventDto.email());
        notificationService.processEvent(eventDto);
        log.info("Processed user event: actionType={}, username={}",
                eventDto.actionType(), eventDto.username());
    }
}

