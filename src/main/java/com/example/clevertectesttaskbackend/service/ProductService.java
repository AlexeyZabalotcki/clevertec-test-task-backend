package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.repository.CardRepository;
import com.example.clevertectesttaskbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CardRepository cardRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchProductException("Product not found"));
    }

    public Product addProducts(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
