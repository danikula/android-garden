package com.danikula.android16.core.storage;

import java.util.HashMap;
import java.util.Map;

public class StrongMemoryStorage<K, T> implements Storage<K, T> {

    private Map<K, T> storage;

    public StrongMemoryStorage() {
        this.storage = new HashMap<K, T>();
    }

    @Override
    public synchronized void put(K key, T value) {
        storage.put(key, value);
    }

    @Override
    public synchronized T get(K key) {
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

}
