package com.example.clevertectesttaskbackend.dto;//package com.example.clevertectesttaskbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountCardDto {
    private Long id;
    private Integer number;
    private String color;
    private String producer;
    private boolean discount;
}
