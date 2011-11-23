package com.danikula.android.garden.storage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class TwoLevelBitmapStorage extends BitmapFileBasedStorage {

    private WeakMemoryStorage<String, Bitmap> inMemoryStorage = new WeakMemoryStorage<String, Bitmap>();

    public TwoLevelBitmapStorage(String storagePath) {
        super(storagePath);
    }

    public TwoLevelBitmapStorage(String storagePath, CompressFormat compressFormat, int quality) {
        super(storagePath, compressFormat, quality);
    }

    @Override
    public synchronized Bitmap get(String key) {
        return inMemoryStorage.contains(key) ? inMemoryStorage.get(key) : super.get(key);
    }

    public synchronized void put(String key, Bitmap value) {
        super.put(key, value);
        inMemoryStorage.put(key, value);
    }
}
