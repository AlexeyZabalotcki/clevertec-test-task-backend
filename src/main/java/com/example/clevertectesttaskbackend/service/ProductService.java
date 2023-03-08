package com.example.clevertectesttaskbackend.service;//package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dao.ProductDao;
import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.entity.Product;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;

    public List<ProductDto> getAll() {
        List<Product> products = productDao.findAll();
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        Product product = productDao.findById(id).orElseThrow(() -> new NoSuchProductException("Product not found"));
        return toDto(product);
    }

    public ProductDto addProduct(ProductDto product) {
        Product entityProduct = productDao.save(toEntity(product));
        return toDto(entityProduct);
    }

    public ProductDto update(ProductDto product) {
        Product entityProduct = productDao.save(toEntity(product));
        return toDto(entityProduct);
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    private Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .producer(dto.getProducer())
                .price(dto.getPrice())
                .discount(dto.isDiscount())
                .build();
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .producer(product.getProducer())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .build();
    }

}
