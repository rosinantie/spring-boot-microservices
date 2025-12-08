package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.dto.CustomerRequest;
import com.example.IP_Session_001.dto.CustomerResponse;
import com.example.IP_Session_001.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<CustomerResponse> getByJwt(@AuthenticationPrincipal Jwt jwt) {
        String userIdStr = jwt.getSubject(); // sub claim
        String email = jwt.getClaim("email");
        log.info("JWT User ID: {}", userIdStr);
        log.info("JWT User email:{}",email);
        UUID userId = UUID.fromString(userIdStr);
        return ResponseEntity.ok(service.getCustomerUsingEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(@AuthenticationPrincipal Jwt jwt) {
        String userIdStr = jwt.getSubject();
        log.info("JWT User ID: {}", userIdStr);
        return ResponseEntity.ok(service.getAllCustomers());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        service.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }
}
