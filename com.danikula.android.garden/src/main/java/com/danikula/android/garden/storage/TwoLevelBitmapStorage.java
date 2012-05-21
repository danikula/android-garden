package com.danikula.android.garden.storage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class TwoLevelBitmapStorage extends BitmapFileBasedStorage {

    private SoftMemoryStorage<String, Bitmap> inMemoryStorage = new SoftMemoryStorage<String, Bitmap>();

    public TwoLevelBitmapStorage(String storagePath) {
        super(storagePath);
    }

    public TwoLevelBitmapStorage(String storagePath, boolean scannable) {
        super(storagePath, scannable);
    }

    public TwoLevelBitmapStorage(String storagePath, boolean scannable, CompressFormat compressFormat, int quality) {
        super(storagePath, scannable, compressFormat, quality);
    }

    @Override
    public synchronized Bitmap get(String key) {
        boolean memCacheContains = inMemoryStorage.contains(key);
        Bitmap cacheItem = memCacheContains ? inMemoryStorage.get(key) : super.get(key);
        if (!memCacheContains) {
            inMemoryStorage.put(key, cacheItem);
        }
        return cacheItem;
    }

    public synchronized void put(String key, Bitmap value) {
        inMemoryStorage.put(key, value);
        super.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return inMemoryStorage.contains(key) || super.contains(key);
    }
}
