package com.danikula.android.garden.cache;

import java.io.File;

import android.graphics.Bitmap;

public class SoftMemoryAndDiscBitmapCache extends TwoLevelBitmapCache {

    public SoftMemoryAndDiscBitmapCache(File storageDir) {
        super(storageDir, new SoftMemoryCache<String, Bitmap>());
    }
}
