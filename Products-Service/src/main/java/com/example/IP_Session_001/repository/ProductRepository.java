package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
