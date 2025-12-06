package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.dto.CustomerRequest;
import com.example.IP_Session_001.dto.CustomerResponse;
import com.example.IP_Session_001.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(service.createCustomer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(service.updateCustomer(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping("/getUsingJWT")
    public ResponseEntity<CustomerResponse> getById(
            @RequestHeader("X-USER") String userIdStr
    ) {
        log.info("X-USER :{}",userIdStr);
        UUID userId = UUID.fromString(userIdStr);
        return ResponseEntity.ok(service.getCustomerUsingJWT(userId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(
            @RequestHeader("X-USER") String userIdStr
    ) {
        log.info("X-USER :{}",userIdStr);
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        service.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }
}
