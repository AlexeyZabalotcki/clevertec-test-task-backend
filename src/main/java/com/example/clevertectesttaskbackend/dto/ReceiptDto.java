package com.example.clevertectesttaskbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReceiptDto {
    private Long id;
    private List<ProductDto> products;
    private byte[] receipt;
    private DiscountCardDto card;
    private BigDecimal totalPrice;

}
