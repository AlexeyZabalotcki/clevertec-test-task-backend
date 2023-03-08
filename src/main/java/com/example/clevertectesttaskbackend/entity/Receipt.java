package com.example.clevertectesttaskbackend.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RECEIPT")
public class Receipt {
    @Id
    private Long id;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Info should contains only letters")
    private String info;
    private List<Product> products;
    private byte[] receipt;
    private BigDecimal totalPrice;
}
