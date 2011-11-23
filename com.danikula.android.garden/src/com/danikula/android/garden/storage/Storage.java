package com.danikula.android.garden.storage;

public interface Storage<K, T> {
    
    void put(K key, T value);
    
    boolean contains(K key);
    
    T get (K key);
    
    void clear();

}
