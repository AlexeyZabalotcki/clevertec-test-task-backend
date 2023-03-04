package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.dto.ProductDto;

import java.math.BigDecimal;

public class ProductDtoTestBuilder implements TestBuilder<ProductDto> {

    private final Long id;
    private final String title;
    private final BigDecimal price;
    private final boolean discount;

    public ProductDtoTestBuilder(Long id, String title, BigDecimal price, boolean discount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    @Override
    public ProductDto build() {
        return ProductDto.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .discount(this.discount)
                .build();
    }
}
