package com.example.IP_Session_001.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "order-events", groupId = "generic-group")
    public void consume(Object message) {
        log.info("[KAFKA-CONSUMER] Received message: {}", message);
    }
}
