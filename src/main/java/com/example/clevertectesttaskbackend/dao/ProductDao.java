package com.example.clevertectesttaskbackend.dao;

import com.example.clevertectesttaskbackend.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends CrudRepository<Product, Long> {
    List<Product> findAll();

    Optional<Product> findById(Long id);
}
