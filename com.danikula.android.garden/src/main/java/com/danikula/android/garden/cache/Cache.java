package com.danikula.android.garden.cache;

public interface Cache<K, T> {
    
    void put(K key, T value);
    
    boolean contains(K key);
    
    T get (K key);
    
    void clear();
    
    void remove(K key);

}
