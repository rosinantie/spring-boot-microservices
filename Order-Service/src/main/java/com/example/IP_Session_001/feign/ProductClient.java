package com.example.IP_Session_001.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:9000/apis/products")
public interface ProductClient {

    @GetMapping("/{id}")
    Object getProductById(@PathVariable Long id);
}
