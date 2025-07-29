package com.velshletter.auth_service.kafka;

import com.velshletter.base_domains.dto.UserEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserKafkaProducer {

    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void sendUserEvent(UserEventDto dto) {
        log.info("Publishing user event to Kafka: actionType={}, username={}, email={}",
                dto.actionType(), dto.username(), dto.email());
        CompletableFuture<SendResult<String, UserEventDto>> future = kafkaTemplate.send(TOPIC, dto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published user event: actionType={}, username={}, offset={}",
                        dto.actionType(), dto.username(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish user event: actionType={}, username={}, error={}",
                        dto.actionType(), dto.username(), ex.getMessage(), ex);
            }
        });
    }
}
