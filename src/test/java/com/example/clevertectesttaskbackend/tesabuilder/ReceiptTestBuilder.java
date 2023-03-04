package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.model.Receipt;

import java.math.BigDecimal;
import java.util.List;

public class ReceiptTestBuilder implements TestBuilder<Receipt> {

    private List<Product> products;
    private byte[] byteArray;
    private BigDecimal totalPrice;

    public ReceiptTestBuilder(List<Product> products, byte[] byteArray, BigDecimal totalPrice) {
        this.products = products;
        this.byteArray = byteArray;
        this.totalPrice = totalPrice;
    }

    @Override
    public Receipt build() {
        return Receipt.builder()
                .products(this.products)
                .receipt(this.byteArray)
                .totalPrice(this.totalPrice)
                .build();
    }
}
