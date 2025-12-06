package com.example.IP_Session_001.service;

import com.example.IP_Session_001.dto.CustomerRequest;
import com.example.IP_Session_001.dto.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(UUID id, CustomerRequest request);

    void deleteCustomer(UUID id);

    CustomerResponse getCustomer(UUID id);

    CustomerResponse getCustomerUsingJWT(UUID id);

    List<CustomerResponse> getAllCustomers();
}
