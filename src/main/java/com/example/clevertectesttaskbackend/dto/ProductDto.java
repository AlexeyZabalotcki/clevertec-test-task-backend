package com.example.clevertectesttaskbackend.dto;//package com.example.clevertectesttaskbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {
    private Long id;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Title should contains only letters")
    private String title;
    private String producer;
    private BigDecimal price;
    private boolean discount;
}
