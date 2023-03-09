package com.example.clevertectesttaskbackend.dto;//package com.example.clevertectesttaskbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountCardDto {
    private Long id;
    @Pattern(regexp = ".*[0-9].*", message = "Number should contains only numbers")
    private Integer number;
    private String color;
    private String producer;
    private boolean discount;
}
