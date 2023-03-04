package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import com.example.clevertectesttaskbackend.model.DiscountCard;
import com.example.clevertectesttaskbackend.repository.CardRepository;
import com.example.clevertectesttaskbackend.tesabuilder.CardDtoTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.CardTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    private static DiscountCardDto expectedDto;
    private static DiscountCard card;

    @BeforeEach
    void setUp() {
        TestBuilder<DiscountCardDto> cardDtoBuilder = new CardDtoTestBuilder(1L, 1234, true);
        expectedDto = cardDtoBuilder.build();

        TestBuilder<DiscountCard> cardBuilder = new CardTestBuilder(1L, 1234, true);
        card = cardBuilder.build();
    }

    @Test
    void checkGetAllShouldReturnAllCards() {
        List<DiscountCardDto> expectedCardsDto = new ArrayList<>(Arrays.asList(expectedDto));
        List<DiscountCard> expectedCards = new ArrayList<>(Arrays.asList(card));

        when(cardRepository.findAll()).thenReturn(expectedCards);

        List<DiscountCardDto> actual = cardService.getAll();

        assertEquals(expectedCardsDto, actual);

        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void checkAddCardShouldAddCardAndReturnDiscountCardDto() {
        when(cardRepository.save(any(DiscountCard.class))).thenReturn(card);

        DiscountCardDto actual = cardService.addCard(expectedDto);

        assertEquals(expectedDto, actual);

        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void checkUpdateShouldUpdateCardAndReturnDiscountCardDto() {
        when(cardRepository.save(any(DiscountCard.class))).thenReturn(card);

        DiscountCardDto actual = cardService.update(expectedDto);

        assertEquals(expectedDto, actual);

        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void checkFindByIdShouldReturnDiscountCardDto() {
        Long id = 1L;

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));

        DiscountCardDto actual = cardService.findById(id);

        assertEquals(expectedDto, actual);

        verify(cardRepository, times(1)).findById(id);
    }

    @Test
    void checkFindByIdShouldTrowNoSuchCardException() {
        assertThrows(NoSuchCardException.class, () -> {
            cardService.findById(1330L);
        });
    }

    @Test
    void checkDeleteByIdShouldDeleteCard() {
        Long id = 1L;
        doNothing().when(cardRepository).deleteById(id);

        cardService.deleteById(id);

        verify(cardRepository, times(1)).deleteById(id);
    }
}
