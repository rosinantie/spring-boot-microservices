package com.example.IP_Session_001.feign;

import com.example.IP_Session_001.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Call the Notification-Service directly (bypassing the gateway) so no gateway JWT is required.
@FeignClient(name = "notification-service", url = "http://localhost:8084/apis/notifications")
public interface NotificationClient {

    // Synchronous call to the Notification-Service REST API to place a notification.
    @PostMapping
    Object sendNotification(@RequestBody Order order);
}