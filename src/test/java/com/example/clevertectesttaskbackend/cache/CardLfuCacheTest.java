package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.builder.CardBuilder;
import com.example.clevertectesttaskbackend.builder.TestBuilder;
import com.example.clevertectesttaskbackend.entity.DiscountCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardLfuCacheTest {
    private Cache cache;

    private DiscountCard card1;
    private DiscountCard card2;

    private TestBuilder<DiscountCard> builder;

    @BeforeEach
    void setUp() {
        builder = new CardBuilder(1L, 123, "blue", "Producer 1", true);
        card1 = builder.build();
        builder = new CardBuilder(2L, 1234, "black", "Producer 1", true);
        card2 = builder.build();
        cache = new CardLfuCache(3);
    }

    @Test
    void checkPutAndGetShouldReturnDiscountCard() {
        cache.put(1, card1);
        Object actual = cache.get(1);

        assertEquals(card1, actual);
    }

    @Test
    void checkGetByIdShouldReturnDiscountCardFromCache() {
        cache.put(1, card1);
        Object actual = cache.getById(card1.getId());
        assertEquals(card1, actual);
    }

    @Test
    public void checkPutNullKeyShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, card1));
    }

    @Test
    void checkEvictDiscountCardShouldReturnsNull() {
        cache.put(1, card1);
        cache.evict(1);
        DiscountCard result = (DiscountCard) cache.get(1);

        assertNull(result);
    }

    @Test
    void checkGetSizeShouldReturnZero() {
        int size = cache.size();

        assertEquals(0, size);
    }

    @Test
    void checkGetSizeShouldReturnIsOne() {
        cache.put(1, card1);

        int size = cache.size();

        assertEquals(1, size);
    }

}
