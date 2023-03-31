package com.example.clevertectesttaskbackend.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * A factory class for creating Cache objects.
 * <p>
 * This class provides method for creating different types of cache objects
 * depending on the requirements of the application.
 * <p>
 * Example usage:
 * <p>
 * CacheFactory factory = new CacheFactory();
 * factory.createCache();
 *
 * @author Zabalotcki Alexey
 */

@Component
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class CacheFactory {

    @Value("{cache.type}")
    private String cacheType;

    private final ProductLruCache lruCache;

    private final ProductLfuCache lfuCache;

    public Cache createCache() {
        switch (cacheType) {
            case "lru":
                return lruCache;
            case "lfu":
                return lfuCache;
            default:
                throw new IllegalArgumentException("Invalid cache type: " + cacheType);
        }
    }
}
