package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.entity.DiscountCard;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class represents a least recently used (LRU) cache for storing discount card objects.
 * The cache is implemented using a LinkedHashMap with access-order mode, so that the least
 * recently used entries are evicted when the cache reaches its maximum size.
 *
 * @author Zabalotcki Alexey
 */

@Component
@Getter
@PropertySource("classpath:application.yml")
public class CardLruCache implements Cache {
    private final LinkedHashMap<DiscountCard, Integer> cache;
    private final int capacity;

    /**
     * Constructs a new CardLruCache object with the specified maximum size.
     *
     * @param capacity the maximum number of entries that the cache can hold inject from application.yml
     */
    public CardLruCache(@Value("${cache.maxSize}") int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
    }

    /**
     * Adds the specified object to the cache.
     *
     * @param key   the object to add to the cache
     * @param value object number in cache queue
     */

    @Override
    public void put(Object key, Object value) {
        if (!(key instanceof DiscountCard) || !(value instanceof Integer)) {
            throw new IllegalArgumentException("Invalid key or value type");
        }

        DiscountCard card = (DiscountCard) key;
        Integer count = (Integer) value;

        if (cache.containsKey(card)) {
            cache.replace(card, count);
        } else if (cache.size() == capacity) {
            DiscountCard leastRecentlyUsed = cache.keySet().iterator().next();
            cache.remove(leastRecentlyUsed);
        }

        cache.put(card, count);
    }

    /**
     * Returns the object from the cache.
     *
     * @param key the object to retrieve from the cache.
     * @return the object from cache.
     */

    @Override
    public Object get(Object key) {
        if (!(key instanceof DiscountCard)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        DiscountCard card = (DiscountCard) key;

        if (!cache.containsKey(card)) {
            return null;
        }
        List<DiscountCard> discountCards = new ArrayList<>(cache.keySet());

        return discountCards.get(0);
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
        for (DiscountCard card : cache.keySet()) {
            if (card.getId().equals(id)) {
                return card;
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
        if (!(key instanceof DiscountCard)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        DiscountCard card = (DiscountCard) key;

        cache.remove(card);
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
