package com.danikula.android.garden.cache;

import android.graphics.Bitmap;

public class LimitedMemoryBitmapCache implements Cache<String, Bitmap> {

    private LruCache<String, Bitmap> lruCache;

    public LimitedMemoryBitmapCache(int bytesLimit) {
        this.lruCache = new LimitedMemoryLruBitmapCache(bytesLimit);
    }

    @Override
    public void put(String key, Bitmap value) {
        lruCache.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return lruCache.get(key) != null;
    }

    @Override
    public Bitmap get(String key) {
        return lruCache.get(key);
    }

    @Override
    public void clear() {
        lruCache.evictAll();
    }

    @Override
    public void remove(String key) {
        lruCache.remove(key);
    }

    private static final class LimitedMemoryLruBitmapCache extends LruCache<String, Bitmap> {

        public LimitedMemoryLruBitmapCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

}
