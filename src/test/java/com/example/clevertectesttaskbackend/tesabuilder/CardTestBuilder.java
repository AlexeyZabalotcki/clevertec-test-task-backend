package com.example.clevertectesttaskbackend.tesabuilder;

import com.example.clevertectesttaskbackend.model.DiscountCard;

public class CardTestBuilder implements TestBuilder<DiscountCard> {
    private final Long id;
    private final Integer number;
    private final boolean discount;

    public CardTestBuilder(Long id, Integer number, boolean discount) {
        this.id = id;
        this.number = number;
        this.discount = discount;
    }

    @Override
    public DiscountCard build() {
        return DiscountCard.builder()
                .id(this.id)
                .number(this.number)
                .discount(this.discount)
                .build();
    }
}
