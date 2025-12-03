package com.example.IP_Session_001.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, Object event) {
        log.info("[KAFKA] Sending to {}: {}", topic, event);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[KAFKA] Failed to send message to {}: {}", topic, ex.getMessage());
            } else {
                log.info("[KAFKA] Message sent successfully to {} with offset {}", topic, result.getRecordMetadata().offset());
            }
        });
    }
}
