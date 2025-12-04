package com.example.IP_Session_001.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(Object message) {
        log.info("[RABBITMQ] Sending email task: {}", message);
        rabbitTemplate.convertAndSend("order-exchange", "order.routing", message);
    }
}
