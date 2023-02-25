package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.exception.NoSuchCardException;
import com.example.clevertectesttaskbackend.service.CardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;
    @InjectMocks
    private CardController cardController;

    private DiscountCardDto expectedCard;

    @BeforeEach
    void setUp() {
        expectedCard = DiscountCardDto.builder()
                .id(1L)
                .number(123)
                .discount(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    private static Stream<Arguments> cardProvider() {
        DiscountCardDto expectedCard = DiscountCardDto.builder()
                .id(1L)
                .number(123)
                .discount(true)
                .build();

        DiscountCardDto expectedCard1 = DiscountCardDto.builder()
                .id(2L)
                .number(1234)
                .discount(true)
                .build();
        return Stream.of(
                Arguments.of(expectedCard),
                Arguments.of(expectedCard1)
        );
    }

    @Test
    void checkFindAllShouldReturnAllCards() {
        List<DiscountCardDto> expectedAllCards = Arrays.asList(expectedCard);

        when(cardService.getAll()).thenReturn(expectedAllCards);

        List<DiscountCardDto> actualAllCards = cardController.getCards();

        assertNotNull(actualAllCards);
        assertEquals(expectedAllCards.size(), actualAllCards.size());
        assertEquals(expectedAllCards.get(0), actualAllCards.get(0));

        verify(cardService, times(1)).getAll();
    }

    @Test
    void checkAddShouldReturnAddedCard() {
        expectedCard.setId(null);

        DiscountCardDto expectedDto = DiscountCardDto.builder()
                .id(1L)
                .number(123)
                .discount(true)
                .build();


        when(cardService.addCard(Mockito.any())).thenReturn(expectedDto);

        ResponseEntity<DiscountCardDto> response = cardController.add(expectedCard);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId().longValue());
        assertEquals(123, response.getBody().getNumber());
        assertTrue(response.getBody().isDiscount());

        verify(cardService, times(1)).addCard(expectedCard);
    }

    @ParameterizedTest
    @MethodSource("cardProvider")
    void checkUpdateShouldReturnUpdatedCard() {
        ResponseEntity<DiscountCardDto> response = cardController.update(expectedCard);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(cardService, times(1)).update(expectedCard);
    }

    @Test
    void checkFindByIdShouldReturnCard() {
        Long id = 1L;

        when(cardService.findById(anyLong())).thenReturn(expectedCard);

        ResponseEntity<DiscountCardDto> response = cardController.findById(id);

        verify(cardService).findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId().longValue());
        assertEquals(123, response.getBody().getNumber());
        assertTrue(response.getBody().isDiscount());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkFindByIdShouldTrowNoSuchCardException() {
        long id = 123L;

        when(cardService.findById(id)).thenThrow(new NoSuchCardException("Check card id"));

        ResponseEntity<DiscountCardDto> response = cardController.findById(id);

        assertEquals("Check card id", response.getBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

        verify(cardService).findById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldReturnOkStatus() {
        Long id = 1L;
        ResponseEntity<DiscountCardDto> response = cardController.deleteById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(cardService, times(1)).deleteById(id);
    }

    @Test
    void checkDeleteByIdShouldReturnException() {
        Long id = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(cardService).deleteById(id);

        ResponseEntity response = cardController.deleteById(id);

        verify(cardService, times(1)).deleteById(id);

        ResponseEntity expectedResponse = new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);

        assertEquals(expectedResponse, response);
    }
}
