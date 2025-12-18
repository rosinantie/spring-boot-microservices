package com.example.IP_Session_001.kafka;

import com.example.IP_Session_001.dto.OrderEventDTO;
import com.example.IP_Session_001.model.Notification;
import com.example.IP_Session_001.repository.NotificationRepository;
import com.example.IP_Session_001.service.NotificationSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationRepository notificationRepository;
    private final NotificationSender notificationSender;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String message) {

        try {
            OrderEventDTO orderEvent = objectMapper.readValue(message, OrderEventDTO.class);

            Notification notification = new Notification();
            notification.setOrderId(orderEvent.getId());
            notification.setMessage("Order placed! Order ID: " + orderEvent.getId());
            notification.setUserEmail("user@example.com");
            notification.setOrderData(message);
            notification.setStatus(Notification.NotificationStatus.CREATED);
            notification.setCreatedAt(Instant.now());

            notificationRepository.save(notification);

            notificationSender.send(notification);

        } catch (Exception e) {
            log.error("Failed to process notification", e);
        }
    }

}
