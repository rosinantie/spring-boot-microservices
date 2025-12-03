package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
