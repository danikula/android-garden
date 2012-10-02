package com.danikula.android.garden.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import android.graphics.Bitmap;

public class TwoLevelBitmapCache extends BitmapFileBasedCache {

    private Cache<String, Bitmap> firstLevelCache;

    public TwoLevelBitmapCache(File storageDir, Cache<String, Bitmap> firstLevelCache) {
        super(storageDir);
        this.firstLevelCache = checkNotNull(firstLevelCache);
    }

    @Override
    public Bitmap get(String key) {
        boolean memCacheContains = firstLevelCache.contains(key);
        Bitmap cacheItem = memCacheContains ? firstLevelCache.get(key) : super.get(key);
        if (!memCacheContains) {
            firstLevelCache.put(key, cacheItem);
        }
        return cacheItem;
    }

    public void put(String key, Bitmap value) {
        firstLevelCache.put(key, value);
        super.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return firstLevelCache.contains(key) || super.contains(key);
    }
}
