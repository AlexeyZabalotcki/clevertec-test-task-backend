package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import com.example.clevertectesttaskbackend.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/")
    public List<DiscountCardDto> getCards() {
        return cardService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<DiscountCardDto> add(@RequestBody DiscountCardDto card) {
        return ResponseEntity.ok(cardService.addCard(card));
    }

    @PutMapping("/update")
    public ResponseEntity<DiscountCardDto> update(@RequestBody DiscountCardDto cardDto) {
        cardService.update(cardDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DiscountCardDto> findById(@PathVariable Long id) {
        DiscountCardDto cardDto;

        try {
            cardDto = cardService.findById(id);
        } catch (NoSuchCardException ex) {
            ex.printStackTrace();
            return new ResponseEntity("Check card id", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(cardDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DiscountCardDto> deleteById(@PathVariable Long id) {
        try {
            cardService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
