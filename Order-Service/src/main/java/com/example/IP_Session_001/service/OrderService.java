package com.example.IP_Session_001.service;

import com.example.IP_Session_001.entity.Order;
<<<<<<< HEAD
import com.example.IP_Session_001.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

//    public Order createOrder(Order order) {
//
//        // Get product
//        ProductResponse product = webClientBuilder.build()
//                .get()
//                .uri("http://PRODUCTS-SERVICE/api/v1/products/" + order.getProductId())
//                .retrieve()
//                .bodyToMono(ProductResponse.class)
//                .block();
//
//        // Get client
//        ClientResponse client = webClientBuilder.build()
//                .get()
//                .uri("http://CUSTOMER-SERVICE/api/v1/customers/" + order.getClientId())
//                .retrieve()
//                .bodyToMono(ClientResponse.class)
//                .block();
//
//        if (client == null || product == null) {
//            throw new RuntimeException("Invalid client or product ID");
//        }
//
//        // Calculate total price
//        order.setTotalPrice(product.getPrice() * order.getQuantity());
//
//        return orderRepository.save(order);
//    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
=======
import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
>>>>>>> 9cdcdc0 (Initial monorepo commit)
}
