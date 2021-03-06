package com.danikula.android.garden.cache;

public class EmptyCache<K, T> implements Cache<K, T>{

    @Override
    public void put(K key, T value) {
        // empty implementation
    }

    @Override
    public boolean contains(K key) {
        return false;
    }

    @Override
    public T get(K key) {
        return null;
    }

    @Override
    public void clear() {
        // empty implementation
    }

    @Override
    public void remove(K key) {
        // empty implementation
    }

}
