package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.builder.ProductBuilder;
import com.example.clevertectesttaskbackend.builder.TestBuilder;
import com.example.clevertectesttaskbackend.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductLruCacheTest {

    private Cache cache;

    private Product product1;
    private Product product2;
    private Product product3;

    private TestBuilder<Product> builder;

    @BeforeEach
    void setUp() {
        builder = new ProductBuilder(1L, "Product 1", "Producer 1", new BigDecimal(123), true);
        product1 = builder.build();
        builder = new ProductBuilder(2L, "Product 2", "Producer 2", new BigDecimal(123), true);
        product2 = builder.build();
        builder = new ProductBuilder(3L, "Product 3", "Producer 3", new BigDecimal(123), true);
        product3 = builder.build();
        cache = new ProductLruCache(3);
    }

    @Test
    void checkPutAndGetShouldPutAndGetProductFromCache() {
        cache.put(product1, 1);

        assertEquals(1, cache.get(product1));
    }

    @Test
    void checkGetByIdShouldReturnDiscountProductFromCache() {
        cache.put(product1, 1);
        Object actual = cache.getById(product1.getId());
        assertEquals(product1, actual);
    }

    @Test
    void checkEvictShouldEvictLastProduct() {
        cache.put(product1, 1);
        cache.put(product2, 2);
        cache.put(product3, 3);

        cache.evict(product1);
        assertNull(cache.get(product1));

    }

    @Test
    void checkClearShouldClearCache() {
        cache.put(product1, 1);
        cache.put(product2, 2);

        cache.clear();

        assertEquals(0, cache.size());
    }

    @Test
    void checkSizeShouldReturnCacheSize() {
        cache.put(product1, 1);
        cache.put(product2, 2);

        assertEquals(2, cache.size());
    }
}
