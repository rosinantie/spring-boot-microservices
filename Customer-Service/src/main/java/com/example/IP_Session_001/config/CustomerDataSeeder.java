package com.example.IP_Session_001.config;

import com.example.IP_Session_001.entity.Customer;
import com.example.IP_Session_001.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the customers table with 5 sample customers on application startup.
 * Runs only when the table is empty, so restarts won't create duplicates.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerDataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            log.info("Customer seeder skipped: {} customer(s) already exist.", customerRepository.count());
            return;
        }

        List<Customer> seedCustomers = List.of(
                Customer.builder()
                        .firstName("Alice")
                        .lastName("Johnson")
                        .email("alice.johnson@example.com")
                        .mobile("9000000001")
                        .status("ACTIVE")
                        .build(),
                Customer.builder()
                        .firstName("Bob")
                        .lastName("Smith")
                        .email("bob.smith@example.com")
                        .mobile("9000000002")
                        .status("ACTIVE")
                        .build(),
                Customer.builder()
                        .firstName("Charlie")
                        .lastName("Brown")
                        .email("charlie.brown@example.com")
                        .mobile("9000000003")
                        .status("ACTIVE")
                        .build(),
                Customer.builder()
                        .firstName("Diana")
                        .lastName("Prince")
                        .email("diana.prince@example.com")
                        .mobile("9000000004")
                        .status("ACTIVE")
                        .build(),
                Customer.builder()
                        .firstName("Ethan")
                        .lastName("Hunt")
                        .email("ethan.hunt@example.com")
                        .mobile("9000000005")
                        .status("ACTIVE")
                        .build()
        );

        customerRepository.saveAll(seedCustomers);
        log.info("Customer seeder inserted {} sample customers.", seedCustomers.size());
    }
}
