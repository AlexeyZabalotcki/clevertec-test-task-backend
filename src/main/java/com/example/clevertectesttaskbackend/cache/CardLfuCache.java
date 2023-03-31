package com.example.clevertectesttaskbackend.cache;

import com.example.clevertectesttaskbackend.entity.DiscountCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * A cache that stores DiscountCard objects using a least-frequently used (LFU) eviction policy.
 * The cache is implemented using a linked hash map to keep track of the frequency of card access.
 * When the cache reaches its maximum capacity, the least frequently used card is evicted from the cache.
 *
 * @author Zabalotcki Alexey
 */

@Component
@PropertySource("classpath:application.yml")
public class CardLfuCache implements Cache {

    private final Map<Object, DiscountCard> cache;
    private final Map<Object, Integer> frequencies;
    private final int capacity;

    public CardLfuCache(@Value("${cache.maxSize}") int capacity) {
        this.cache = new LinkedHashMap<>(capacity);
        this.frequencies = new HashMap<>();
        this.capacity = capacity;
    }

    /**
     * Adds the specified card to the cache, evicting the least frequently used card if necessary.
     * Increases the frequency count for the added card.
     *
     * @param key   the card to add to the cache
     * @param value the value associated with the card
     */

    @Override
    public void put(Object key, Object value) {
        if (!(key instanceof Integer) || !(value instanceof DiscountCard)) {
            throw new IllegalArgumentException("Invalid key or value type");
        }

        DiscountCard card = (DiscountCard) value;

        if (cache.size() == capacity) {
            int minFrequency = Collections.min(frequencies.values());
            List<Object> leastFrequentKeys = new ArrayList<>();
            for (Map.Entry<Object, Integer> entry : frequencies.entrySet()) {
                if (entry.getValue() == minFrequency) {
                    leastFrequentKeys.add(entry.getKey());
                }
            }
            Object leastFrequentKey = leastFrequentKeys.get(0);
            cache.remove(leastFrequentKey);
            frequencies.remove(leastFrequentKey);
        }

        cache.put(key, card);
        frequencies.put(key, 1);
    }

    /**
     * Returns the object from the cache.
     *
     * @param key the object to retrieve from the cache.
     * @return the object from cache.
     */

    @Override
    public Object get(Object key) {
        if (!(key instanceof Integer)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        if (!cache.containsKey(key)) {
            return null;
        }

        int frequency = frequencies.get(key);
        frequencies.put(key, frequency + 1);

        return cache.get(key);
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
        List<DiscountCard> discountCards = new ArrayList<>(cache.values());
        for (DiscountCard card : discountCards) {
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
        if (!(key instanceof Integer)) {
            throw new IllegalArgumentException("Invalid key type");
        }

        if (!cache.containsKey(key)) {
            return;
        }

        cache.remove(key);
        frequencies.remove(key);
    }

    /**
     * Clears cache
     */

    @Override
    public void clear() {
        cache.clear();
        frequencies.clear();
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
