package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductDto> findAll() {
        return productService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> add(@RequestBody ProductDto product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto product) {
        productService.update(product);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        ProductDto product;

        try {
            product = productService.findById(id);
        } catch (NoSuchProductException ex) {
            ex.printStackTrace();
            return new ResponseEntity("Check product id", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductDto> deleteById(@PathVariable Long id) {
        try {
            productService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
