package com.danikula.android.garden.cache;

import java.io.File;

public class LimitedMemoryAndDiscBitmapCache extends TwoLevelBitmapCache {

    public LimitedMemoryAndDiscBitmapCache(File storageDir, int bytesLimit) {
        super(storageDir, new LimitedMemoryBitmapCache(bytesLimit));
    }

}
