package com.example.IP_Session_001.service.impl;

import com.example.IP_Session_001.dto.OrderEventDTO;
import com.example.IP_Session_001.model.Notification;
import com.example.IP_Session_001.repository.NotificationRepository;
import com.example.IP_Session_001.service.NotificationSender;
import com.example.IP_Session_001.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSender notificationSender;

    @Override
    public Notification placeNotification(OrderEventDTO orderEvent) {
        log.info("[API] Received notification request for order: {}", orderEvent);

        Notification notification = new Notification();
        notification.setOrderId(orderEvent.getId());
        notification.setMessage("Order placed! Order ID: " + orderEvent.getId());
        notification.setUserEmail("user@example.com");
        notification.setOrderData(orderEvent.toString());
        notification.setStatus(Notification.NotificationStatus.CREATED);
        notification.setCreatedAt(Instant.now());

        notificationRepository.save(notification);

        notificationSender.send(notification);

        log.info("[API] Notification processed for order {}", orderEvent.getId());
        return notification;
    }
}