package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.model.Product;

import java.math.BigDecimal;

public class ProductTestBuilder implements TestBuilder<Product> {

    private final Long id;
    private final String title;
    private final BigDecimal price;
    private final boolean discount;

    public ProductTestBuilder(Long id, String title, BigDecimal price, boolean discount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    @Override
    public Product build() {
        return Product.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .discount(this.discount)
                .build();
    }
}
