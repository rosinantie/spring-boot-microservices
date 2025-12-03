package com.example.IP_Session_001.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:9000/apis/customers")
public interface CustomerClient {

    @GetMapping("/{id}")
    Object getCustomerById(@PathVariable Long id);
}
