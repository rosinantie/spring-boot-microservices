package com.example.IP_Session_001.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String mongoId;

    private Long orderId;
    private String message;
    private String userEmail;
    private String orderData;

    private NotificationStatus status;   // 👈 STATUS
    private Instant createdAt;            // 👈 TIMESTAMP

    public enum NotificationStatus {
        CREATED,   // stored in DB
        SENT,      // sent successfully (email / push)
        FAILED     // sending failed
    }

}

