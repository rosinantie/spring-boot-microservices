package com.example.IP_Session_001.service;

import com.example.IP_Session_001.entity.Order;
import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
}
