package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.dto.OrderEventDTO;
import com.example.IP_Session_001.model.Notification;
import com.example.IP_Session_001.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotifcationController {

    private final NotificationService notificationService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from NotificationController!";
    }

    // Synchronous REST API to place a notification (called directly by Order-Service via Feign).
    // This is the third delivery architecture, alongside the Kafka and RabbitMQ consumers.
    @PostMapping
    public ResponseEntity<Notification> placeNotification(@RequestBody OrderEventDTO orderEvent) {
        Notification notification = notificationService.placeNotification(orderEvent);
        return ResponseEntity.ok(notification);
    }
}