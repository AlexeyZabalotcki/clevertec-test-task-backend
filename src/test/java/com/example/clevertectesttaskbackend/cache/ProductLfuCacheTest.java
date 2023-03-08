package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.builder.ProductBuilder;
import com.example.clevertectesttaskbackend.builder.TestBuilder;
import com.example.clevertectesttaskbackend.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductLfuCacheTest {

    private Cache cache;

    private Product product1;
    private Product product2;

    private TestBuilder<Product> builder;


    @BeforeEach
    void setUp() {
        builder = new ProductBuilder(1L, "Product 1", "Producer 1", new BigDecimal(123), true);
        product1 = builder.build();
        builder = new ProductBuilder(2L, "Product 2", "Producer 2", new BigDecimal(123), true);
        product2 = builder.build();
        cache = new ProductLfuCache(3);
    }

    @Test
    void checkPutAndGetShouldReturnProduct() {
        cache.put(1, product1);
        Product result = (Product) cache.get(1);

        assertNotNull(result);
        assertEquals(product1, result);
    }

    @Test
    void checkGetByIdShouldReturnDiscountProductFromCache() {
        cache.put(1, product1);
        Object actual = cache.getById(product1.getId());
        assertEquals(product1, actual);
    }

    @Test
    public void checkPutNullKeyShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, product1));
    }

    @Test
    void checkGetShouldReturnSecondProduct() {
        cache.put(1, product1);
        cache.put(2, product2);
        Product result = (Product) cache.get(2);

        assertNotNull(result);
        assertEquals(product2, result);
    }

    @Test
    void checkEvictProductShouldReturnsNull() {
        cache.put(1, product1);
        cache.evict(1);
        Product result = (Product) cache.get(1);

        assertNull(result);
    }

    @Test
    void checkGetSizeShouldReturnZero() {
        int size = cache.size();

        assertEquals(0, size);
    }

    @Test
    void checkGetSizeShouldReturnIsOne() {
        cache.put(1, product1);

        int size = cache.size();

        assertEquals(1, size);
    }
}
