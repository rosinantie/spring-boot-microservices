package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
