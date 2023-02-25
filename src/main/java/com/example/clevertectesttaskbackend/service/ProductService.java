package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchProductException("Product not found"));
        return toDto(product);
    }

    public ProductDto addProducts(ProductDto product) {
        Product entityProduct = productRepository.save(toEntity(product));
        return toDto(entityProduct);
    }

    public ProductDto update(ProductDto product) {
        Product entityProduct = productRepository.save(toEntity(product));
        return toDto(entityProduct);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .discount(dto.isDiscount())
                .build();
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .build();
    }

}
