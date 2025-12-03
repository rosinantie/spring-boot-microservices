package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.entity.Order;
<<<<<<< HEAD
import com.example.IP_Session_001.service.OrderService;

import lombok.RequiredArgsConstructor;
=======
import com.example.IP_Session_001.feign.CustomerClient;
import com.example.IP_Session_001.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> 9cdcdc0 (Initial monorepo commit)

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.util.List;

=======
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
>>>>>>> 9cdcdc0 (Initial monorepo commit)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
<<<<<<< HEAD

//    @PostMapping
//    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
//        return ResponseEntity.ok(orderService.createOrder(order));
//    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
=======
    private final CustomerClient customerClient;

    // CREATE (Add Order)
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("[CREATE] Received request to create order: {}", order);
        Order savedOrder = orderService.createOrder(order);
        log.info("[CREATE] Order created successfully: {}", savedOrder);
        return ResponseEntity.ok(savedOrder);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("[READ ALL] Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        log.info("[READ ALL] Total orders fetched: {}", orders.size());
        return ResponseEntity.ok(orders);
    }

    // READ ONE WITH CUSTOMER (Resilience4j + Feign)
    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackCustomer")
    @Retry(name = "customerRetry", fallbackMethod = "fallbackCustomer")
    @RateLimiter(name = "customerRateLimiter", fallbackMethod = "fallbackCustomer")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderWithCustomer(@PathVariable Long id) {
        log.info("[READ] Fetching order with id: {}", id);
        Order order = orderService.getOrderById(id);
        log.info("[READ] Order found: {}", order);

        try {
            log.info("[FEIGN] Calling Customer Service for clientId: {}", order.getClientId());
            Object customer = customerClient.getCustomerById(order.getClientId());
            log.info("[FEIGN] Customer response received: {}", customer);

            Map<String, Object> response = new HashMap<>();
            response.put("order", order);
            response.put("customer", customer);
            log.info("[READ] Returning combined order and customer response for order id {}: {}", id, response);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("[ERROR] Exception while calling Customer Service for order id {}: {}", id, ex.getMessage());
            throw ex; // Resilience4j will handle fallback
        }
    }

    // FALLBACK METHOD
    public ResponseEntity<Map<String, Object>> fallbackCustomer(Long id, Throwable ex) {
        log.warn("[FALLBACK] Customer Service is down for order id {}. Returning fallback response. Error: {}", id, ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        Order order = orderService.getOrderById(id);

        response.put("order", order);
        response.put("customer", "Customer Service is DOWN. Returning fallback response.");
        response.put("error", ex.getMessage());

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        log.info("[UPDATE] Updating order id {} with data: {}", id, order);
        Order updatedOrder = orderService.updateOrder(id, order);
        log.info("[UPDATE] Order updated successfully: {}", updatedOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        log.info("[DELETE] Deleting order with id: {}", id);
        orderService.deleteOrder(id);
        log.info("[DELETE] Order deleted successfully with id: {}", id);
        return ResponseEntity.ok("Order deleted successfully");
>>>>>>> 9cdcdc0 (Initial monorepo commit)
    }
}
