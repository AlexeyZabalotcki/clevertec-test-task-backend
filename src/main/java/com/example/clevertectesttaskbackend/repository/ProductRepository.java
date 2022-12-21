package com.example.clevertectesttaskbackend.repository;

import com.example.clevertectesttaskbackend.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

    Product findByTitle(String title);
}
