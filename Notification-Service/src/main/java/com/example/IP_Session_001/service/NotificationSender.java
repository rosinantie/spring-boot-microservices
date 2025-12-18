package com.example.IP_Session_001.service;

import com.example.IP_Session_001.model.Notification;
import com.example.IP_Session_001.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSender {

    private final NotificationRepository notificationRepository;

    public void send(Notification notification) {
        try {
            // Simulate sending email / push
            log.info("Sending notification to {}", notification.getUserEmail());

            // TODO: integrate Email / Push service here

            notification.setStatus(Notification.NotificationStatus.SENT);
            notificationRepository.save(notification);

            log.info("Notification SENT for order {}", notification.getOrderId());

        } catch (Exception ex) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notificationRepository.save(notification);

            log.error("Notification FAILED for order {}", notification.getOrderId(), ex);
        }
    }
}
