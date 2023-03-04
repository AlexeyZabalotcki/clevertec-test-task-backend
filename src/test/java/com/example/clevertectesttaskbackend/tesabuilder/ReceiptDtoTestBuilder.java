package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;

import java.math.BigDecimal;
import java.util.List;

public class ReceiptDtoTestBuilder implements TestBuilder<ReceiptDto> {

    private List<ProductDto> products;
    private byte[] byteArray;
    private BigDecimal totalPrice;

    public ReceiptDtoTestBuilder(List<ProductDto> products, byte[] byteArray, BigDecimal totalPrice) {
        this.products = products;
        this.byteArray = byteArray;
        this.totalPrice = totalPrice;
    }

    @Override
    public ReceiptDto build() {
        return ReceiptDto.builder()
                .products(this.products)
                .receipt(this.byteArray)
                .totalPrice(this.totalPrice)
                .build();
    }
}
