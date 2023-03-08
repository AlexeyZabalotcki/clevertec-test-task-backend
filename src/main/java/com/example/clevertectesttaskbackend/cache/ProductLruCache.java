package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.entity.Product;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * This class represents a least recently used (LRU) cache for storing product objects.
 * The cache is implemented using a LinkedHashMap with access-order mode, so that the least
 * recently used entries are evicted when the cache reaches its maximum size.
 *
 * @author Zabalotcki Alexey
 */

@Component
@Getter
@PropertySource("classpath:application.yml")
public class ProductLruCache implements Cache {
    private final LinkedHashMap<Product, Integer> cache;
    private final int capacity;

    /**
     * Constructs a new ProductLruCache object with the specified maximum size.
     *
     * @param capacity the maximum number of entries that the cache can hold inject from application.yml
     */

    public ProductLruCache(@Value("${cache.maxSize}") int capacity) {
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Adds the specified object to the cache.
     *
     * @param key   the object to add to the cache
     * @param value object number in cache queue
     */

    @Override
    public void put(Object key, Object value) {
        if (!(key instanceof Product) || !(value instanceof Integer)) {
            throw new IllegalArgumentException("Invalid key or value type");
        }

        Product product = (Product) key;
        Integer count = (Integer) value;

        if (cache.containsKey(product)) {
            cache.replace(product, count);
        } else if (cache.size() == capacity) {
            Product leastRecentlyUsed = cache.keySet().iterator().next();
            cache.remove(leastRecentlyUsed);
        }

        cache.put(product, count);
    }

    /**
     * Returns the object from the cache.
     *
     * @param key the object to retrieve from the cache.
     * @return the object from cache.
     */

    @Override
    public Object get(Object key) {
        if (!(key instanceof Product)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        Product product = (Product) key;

        if (!cache.containsKey(product)) {
            return null;
        }

        return cache.get(product);
    }

    /**
     * Returns the object with the specified ID from the cache, or null if the object
     * is not present in the cache.
     *
     * @param id the ID of the object to retrieve from the cache
     * @return the object with the specified ID, or null if the object is not in the cache
     */

    @Override
    public Object getById(Long id) {
        for (Product product : cache.keySet()) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Removes the specified object from the cache.
     *
     * @param key the object to remove from the cache
     */

    @Override
    public void evict(Object key) {
        if (!(key instanceof Product)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        Product product = (Product) key;

        cache.remove(product);
    }

    /**
     * Clears cache
     */

    @Override
    public void clear() {
        cache.clear();
    }

    /**
     * Returns the current size of the cache.
     *
     * @return the number of entries currently in the cache
     */

    @Override
    public int size() {
        return cache.size();
    }
}
