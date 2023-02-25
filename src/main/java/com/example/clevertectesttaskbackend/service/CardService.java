package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import com.example.clevertectesttaskbackend.model.DiscountCard;
import com.example.clevertectesttaskbackend.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;


    public List<DiscountCardDto> getAllCards() {
        List<DiscountCard> cards = cardRepository.findAll();
        return cards.stream().map(this::toDto).collect(Collectors.toList());
    }

    public DiscountCardDto addCard(DiscountCardDto card) {
        DiscountCard entityCard = cardRepository.save(toEntity(card));
        return toDto(entityCard);
    }

    public DiscountCardDto update(DiscountCardDto card) {
        DiscountCard entityCard = cardRepository.save(toEntity(card));
        return toDto(entityCard);
    }

    public DiscountCardDto findCardById(Long id) {
        DiscountCard card = cardRepository.findById(id).orElseThrow(() -> new NoSuchCardException("Card not found"));
        return toDto(card);
    }

    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    private DiscountCard toEntity(DiscountCardDto dto) {
        return DiscountCard.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .discount(dto.isDiscount())
                .build();
    }

    private DiscountCardDto toDto(DiscountCard card) {
        return DiscountCardDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .discount(card.isDiscount())
                .build();
    }


}
