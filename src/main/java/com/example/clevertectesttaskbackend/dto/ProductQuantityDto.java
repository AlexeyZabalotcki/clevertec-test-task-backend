package com.example.clevertectesttaskbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductQuantityDto {
    private Long id;
    private String title;
    private Integer quantity;
    private BigDecimal price;
    private boolean discount;
}
