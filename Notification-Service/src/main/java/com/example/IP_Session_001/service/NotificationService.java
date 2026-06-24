package com.example.IP_Session_001.service;

import com.example.IP_Session_001.dto.OrderEventDTO;
import com.example.IP_Session_001.model.Notification;

public interface NotificationService {

    // Build, persist and send a notification for the given order event.
    Notification placeNotification(OrderEventDTO orderEvent);
}