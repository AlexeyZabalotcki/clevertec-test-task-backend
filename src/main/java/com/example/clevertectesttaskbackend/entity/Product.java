package com.example.clevertectesttaskbackend.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCTS")
public class Product {
    @Id
    private Long id;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Title should contains only letters")
    private String title;
    private String producer;
    private BigDecimal price;
    private boolean discount;
}
