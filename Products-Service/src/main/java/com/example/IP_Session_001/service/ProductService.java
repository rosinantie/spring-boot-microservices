package com.example.IP_Session_001.service;

import com.example.IP_Session_001.entity.Product;
import com.example.IP_Session_001.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    @Cacheable(value = "product", key = "#productId")
    public Product getProduct(Long productId) {
        log.info("Fetching product from DB...");
        return repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public List<Product> getAll() {
        return repo.findAll();
    }
}
