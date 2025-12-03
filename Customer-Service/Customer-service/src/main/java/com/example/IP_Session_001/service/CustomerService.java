package com.example.IP_Session_001.service;

import com.example.IP_Session_001.dto.CustomerRequest;
import com.example.IP_Session_001.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);

    CustomerResponse getCustomer(Long id);

    List<CustomerResponse> getAllCustomers();
}
