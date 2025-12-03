package com.example.IP_Session_001.service;

import com.example.IP_Session_001.entity.Product;
import com.example.IP_Session_001.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Product> getAll() {
        return repo.findAll();
    }
}
