package com.example.IP_Session_001.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaOrderConsumer {
    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String message) {
        log.info("[NOTIFICATION SERVICE] Received event: {}", message);
        // TODO: Process further
        // Example: send email, send push notification, save into DB, etc.
    }
}
