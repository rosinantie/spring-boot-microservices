package com.example.IP_Session_001.rabbitMq;

import com.example.IP_Session_001.data.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitOrderConsumer {

    // Listen to the same queue that OrderService sends messages to
    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void consumeOrderEvent(Order order) {
        log.info("[NOTIFICATION SERVICE] Received RabbitMQ event: {}", order);

        // TODO: process further
        // Example: send email, send push notification, save into DB, etc.
    }
}
