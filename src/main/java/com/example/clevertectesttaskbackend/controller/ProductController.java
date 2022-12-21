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
        // add exceptions
        return ResponseEntity.ok(productService.addProducts(product));
    }

    @PutMapping("/update")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        //add exceptions
        productService.update(product);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/check")
    public void printCheck(@RequestParam Long id, Integer quantity) {
        try {
            productService.getCheck(id, quantity);
        } catch (NoSuchProductException ex) {
            ex.printStackTrace();
//            return new ResponseEntity("id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
//        return new ResponseEntity.;
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
            productService .deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
