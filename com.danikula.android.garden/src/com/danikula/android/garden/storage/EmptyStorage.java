package com.danikula.android.garden.storage;

public class EmptyStorage<K, T> implements Storage<K, T>{

    @Override
    public void put(K key, T value) {
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
    }

    @Override
    public void remove(K key) {
    }

}
