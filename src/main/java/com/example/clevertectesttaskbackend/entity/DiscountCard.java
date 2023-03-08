package com.example.clevertectesttaskbackend.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DISCOUNT_CARD")
public class DiscountCard {
    @Id
    private Long id;
    @Pattern(regexp = ".*[0-9].*", message = "Number should contains only numbers")
    private Integer number;
    private String color;
    private String producer;
    private boolean discount;
}
