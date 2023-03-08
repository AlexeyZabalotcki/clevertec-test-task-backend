package com.example.clevertectesttaskbackend.builder;

import com.example.clevertectesttaskbackend.entity.DiscountCard;

public class CardBuilder implements TestBuilder<DiscountCard> {
    private Long id;
    private Integer number;
    private String color;
    private String producer;
    private boolean discount;

    public CardBuilder(Long id, Integer number, String color, String producer, boolean discount) {
        this.id = id;
        this.number = number;
        this.color = color;
        this.producer = producer;
        this.discount = discount;
    }

    @Override
    public DiscountCard build() {
        DiscountCard card = new DiscountCard();
        card.setId(this.id);
        card.setNumber(this.number);
        card.setProducer(this.producer);
        card.setColor(this.color);
        card.setDiscount(this.discount);
        return card;

    }
}
