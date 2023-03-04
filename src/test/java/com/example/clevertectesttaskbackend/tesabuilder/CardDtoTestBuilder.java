package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;

public class CardDtoTestBuilder implements TestBuilder<DiscountCardDto> {
    private final Long id;
    private final Integer number;
    private final boolean discount;

    public CardDtoTestBuilder(Long id, Integer number, boolean discount) {
        this.id = id;
        this.number = number;
        this.discount = discount;
    }

    @Override
    public DiscountCardDto build() {
        return DiscountCardDto.builder()
                .id(this.id)
                .number(this.number)
                .discount(this.discount)
                .build();
    }
}
