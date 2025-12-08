package com.example.IP_Session_001.service.impl;

import com.example.IP_Session_001.dto.CustomerRequest;
import com.example.IP_Session_001.dto.CustomerResponse;
import com.example.IP_Session_001.entity.Customer;
import com.example.IP_Session_001.repository.CustomerRepository;
import com.example.IP_Session_001.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .mobile(request.mobile())
                .build();

        Customer saved = repository.save(customer);

        return toResponse(saved);
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setMobile(request.mobile());

        return toResponse(repository.save(customer));
    }

    @Override
    public void deleteCustomer(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public CustomerResponse getCustomer(UUID id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerUsingJWT(UUID id) {
        Customer customer = repository.findById(id).orElseThrow(()-> new RuntimeException("Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerUsingEmail(String email) {
        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        return toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getMobile()
        );
    }
}
