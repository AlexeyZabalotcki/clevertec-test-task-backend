package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dao.CardDao;
import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.entity.DiscountCard;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import org.junit.jupiter.api.AfterEach;
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
    private CardDao cardDao;

    private DiscountCardDto expectedDto;
    private DiscountCard card;

    @BeforeEach
    void setUp() {
        expectedDto = DiscountCardDto.builder()
                .id(1L)
                .number(123)
                .discount(true)
                .build();

        card = DiscountCard.builder()
                .id(1L)
                .number(123)
                .discount(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkGetAllShouldReturnAllCards() {
        List<DiscountCardDto> expectedCardsDto = new ArrayList<>(Arrays.asList(expectedDto));
        List<DiscountCard> expectedCards = new ArrayList<>(Arrays.asList(card));

        when(cardDao.findAll()).thenReturn(expectedCards);

        List<DiscountCardDto> actual = cardService.getAll();

        assertEquals(expectedCardsDto, actual);

        verify(cardDao, times(1)).findAll();
    }

    @Test
    void checkAddCardShouldAddCardAndReturnDiscountCardDto() {
        when(cardDao.save(any(DiscountCard.class))).thenReturn(card);

        DiscountCardDto actual = cardService.addCard(expectedDto);

        assertEquals(expectedDto, actual);

        verify(cardDao, times(1)).save(card);
    }

    @Test
    void checkUpdateShouldUpdateCardAndReturnDiscountCardDto() {
        when(cardDao.save(any(DiscountCard.class))).thenReturn(card);

        DiscountCardDto actual = cardService.update(expectedDto);

        assertEquals(expectedDto, actual);

        verify(cardDao, times(1)).save(card);
    }

    @Test
    void checkFindByIdShouldReturnDiscountCardDto() {
        Long id = 1L;

        when(cardDao.findById(id)).thenReturn(Optional.of(card));

        DiscountCardDto actual = cardService.findById(id);

        assertEquals(expectedDto, actual);

        verify(cardDao, times(1)).findById(id);
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
        doNothing().when(cardDao).deleteById(id);

        cardService.deleteById(id);

        verify(cardDao, times(1)).deleteById(id);
    }
}
