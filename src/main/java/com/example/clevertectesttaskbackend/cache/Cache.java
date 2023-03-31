package com.example.clevertectesttaskbackend.cache;

/**
 * Interface that defines common cache operations.
 *
 * @author Zabalotcki Alexey
 */

public interface Cache {

    void put(Object key, Object value);

    Object get(Object key);

    Object getById(Long id);

    void evict(Object key);

    void clear();

    int size();

}
