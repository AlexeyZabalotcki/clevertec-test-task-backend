package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.model.DiscountCard;
import com.example.clevertectesttaskbackend.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;


    public List<DiscountCard> getAllCards() {
        return cardRepository.findAll();
    }

    public DiscountCard addCard(DiscountCard card) {
        return cardRepository.save(card);
    }

    public DiscountCard update(DiscountCard card) {
        return cardRepository.save(card);
    }

    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }
}
