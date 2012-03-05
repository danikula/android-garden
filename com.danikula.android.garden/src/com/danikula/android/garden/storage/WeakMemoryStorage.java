package com.danikula.android.garden.storage;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class WeakMemoryStorage<K, T> implements Storage<K, T> {

    private Map<K, SoftReference<T> > storage;

    public WeakMemoryStorage() {
        this.storage = new HashMap<K, SoftReference<T> >();
    }

    @Override
    public synchronized void put(K key, T value) {
        storage.put(key, new SoftReference<T>(value));
    }

    @Override
    public synchronized T get(K key) {
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
