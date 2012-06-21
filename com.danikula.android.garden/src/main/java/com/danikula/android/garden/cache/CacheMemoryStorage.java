package com.danikula.android.garden.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CacheMemoryStorage<K, T> implements Cache<K, T> {

    private Map<K, T> storage;

    public CacheMemoryStorage() {
        this.storage = Collections.synchronizedMap(new HashMap<K, T>());
    }

    @Override
    public void put(K key, T value) {
        storage.put(key, value);
    }

    @Override
    public T get(K key) {
        return storage.get(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean contains(K key) {
        return storage.containsKey(key);
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

}
