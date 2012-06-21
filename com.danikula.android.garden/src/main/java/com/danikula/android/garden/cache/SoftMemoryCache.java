package com.danikula.android.garden.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SoftMemoryCache<K, T> implements Cache<K, T> {

    private Map<K, SoftReference<T>> storage;

    public SoftMemoryCache() {
        this.storage = Collections.synchronizedMap(new HashMap<K, SoftReference<T>>());
    }

    @Override
    public void put(K key, T value) {
        storage.put(key, new SoftReference<T>(value));
    }

    @Override
    public T get(K key) {
        return contains(key) ? storage.get(key).get() : null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean contains(K key) {
        return storage.containsKey(key) && storage.get(key).get() != null;
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }
}
