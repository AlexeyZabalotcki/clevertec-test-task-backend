package com.example.clevertectesttaskbackend.service;//package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dao.CardDao;
import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.entity.DiscountCard;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardDao cardDao;


    public List<DiscountCardDto> getAll() {
        List<DiscountCard> cards = cardDao.findAll();
        return cards.stream().map(this::toDto).collect(Collectors.toList());
    }

    public DiscountCardDto addCard(DiscountCardDto card) {
        DiscountCard entityCard = cardDao.save(toEntity(card));
        return toDto(entityCard);
    }

    public DiscountCardDto update(DiscountCardDto card) {
        DiscountCard entityCard = cardDao.save(toEntity(card));
        return toDto(entityCard);
    }

    public DiscountCardDto findById(Long id) {
        DiscountCard card = cardDao.findById(id).orElseThrow(() -> new NoSuchCardException("Card not found"));
        return toDto(card);
    }

    public void deleteById(Long id) {
        cardDao.deleteById(id);
    }

    private DiscountCard toEntity(DiscountCardDto dto) {
        return DiscountCard.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .color(dto.getColor())
                .producer(dto.getProducer())
                .discount(dto.isDiscount())
                .build();
    }

    private DiscountCardDto toDto(DiscountCard card) {
        return DiscountCardDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .color(card.getColor())
                .producer(card.getProducer())
                .discount(card.isDiscount())
                .build();
    }


}
