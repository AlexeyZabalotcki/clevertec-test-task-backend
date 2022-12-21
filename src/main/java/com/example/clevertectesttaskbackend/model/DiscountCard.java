package com.example.clevertectesttaskbackend.model;

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
@Table(name = "discount_card")
public class DiscountCard {
    @Id
    private Long id;
    private Integer number;
    private boolean discount;
}
