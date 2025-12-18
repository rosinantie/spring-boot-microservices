package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    // Custom query methods if needed

}
