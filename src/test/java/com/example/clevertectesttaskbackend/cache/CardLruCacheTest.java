package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.builder.CardBuilder;
import com.example.clevertectesttaskbackend.builder.TestBuilder;
import com.example.clevertectesttaskbackend.entity.DiscountCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CardLruCacheTest {

    private Cache cache;

    private DiscountCard card1;
    private DiscountCard card2;
    private DiscountCard card3;

    private TestBuilder<DiscountCard> builder;

    @BeforeEach
    void setUp() {
        builder = new CardBuilder(1L, 123, "blue", "Producer 1", true);
        card1 = builder.build();
        builder = new CardBuilder(2L, 1234, "black", "Producer 1", true);
        card2 = builder.build();
        builder = new CardBuilder(3L, 12345, "red", "Producer 1", true);
        card3 = builder.build();
        cache = new CardLruCache(4);
    }

    @Test
    void checkPutAndGetShouldPutAndGetDiscountCardFromCache() {
        cache.put(card1, 1);
        Object actual = cache.get(card1);
        assertEquals(card1, actual);
    }

    @Test
    void checkGetByIdShouldReturnDiscountCardFromCache() {
        cache.put(card1, 1);
        Object actual = cache.getById(card1.getId());
        assertEquals(card1, actual);
    }

    @Test
    void checkEvictShouldEvictLastDiscountCard() {
        cache.put(card1, 1);
        cache.put(card2, 2);
        cache.put(card3, 3);

        cache.evict(card1);
        assertNull(cache.get(card1));

        cache.evict(card2);
        assertNull(cache.get(card2));
    }

    @Test
    void checkClearShouldClearCache() {
        cache.put(card1, 1);
        cache.put(card2, 2);

        cache.clear();

        assertEquals(0, cache.size());
    }

    @Test
    void checkSizeShouldReturnCacheSize() {
        cache.put(card1, 1);
        cache.put(card2, 2);

        assertEquals(2, cache.size());
    }

}
