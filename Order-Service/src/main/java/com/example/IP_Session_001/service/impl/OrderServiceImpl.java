package com.example.IP_Session_001.service.impl;

import com.example.IP_Session_001.entity.Order;
import com.example.IP_Session_001.kafka.KafkaProducerService;
import com.example.IP_Session_001.rabbit.RabbitMQProducerService;
import com.example.IP_Session_001.repository.OrderRepository;
import com.example.IP_Session_001.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducer;
    private final RabbitMQProducerService rabbitMQProducer;

    @Override
    public Order createOrder(Order order) {
        Order saved = orderRepository.save(order);

        // Send to Kafka
        kafkaProducer.sendEvent("order-events", saved);


        return saved;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);
        existingOrder.setClientId(order.getClientId());
        existingOrder.setProductId(order.getProductId());
        existingOrder.setQuantity(order.getQuantity());
        existingOrder.setTotalPrice(order.getTotalPrice());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
