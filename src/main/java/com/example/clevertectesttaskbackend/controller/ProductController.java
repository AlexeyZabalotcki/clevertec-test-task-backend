package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody Product product) {
        if (product.getId() != null && product.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (product.getTitle() == null || product.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(productService.addProducts(product));
    }

    @PutMapping("/update")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        if (product.getId() == null || product.getId() == 0) {
            return new ResponseEntity("Error: id MUST be fill", HttpStatus.NOT_ACCEPTABLE);
        }
        if (product.getTitle() == null || product.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: you MUST fill title form", HttpStatus.NOT_ACCEPTABLE);
        }
        productService.update(product);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> printCheck(@RequestParam Long id, Integer quantity) {
        try {
            productService.getCheck(id, quantity);
        } catch (NoSuchProductException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = null;

        try {
            product = productService.findProductById(id);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteById(@PathVariable Long id) {
        try {
            productService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
