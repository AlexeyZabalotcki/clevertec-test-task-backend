package com.example.clevertectesttaskbackend.builder;

import com.example.clevertectesttaskbackend.entity.Product;

import java.math.BigDecimal;

public class ProductBuilder implements TestBuilder<Product> {
    private Long id;
    private String title;
    private String producer;
    private BigDecimal price;
    private boolean discount;

    public ProductBuilder(Long id, String title, String producer, BigDecimal price, boolean discount) {
        this.id = id;
        this.title = title;
        this.producer = producer;
        this.price = price;
        this.discount = discount;
    }

    @Override
    public Product build() {
        Product product = new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setProducer(this.producer);
        product.setPrice(this.price);
        product.setDiscount(this.discount);
        return product;

    }
}
