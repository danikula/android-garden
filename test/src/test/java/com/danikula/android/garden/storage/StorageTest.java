package com.danikula.android.garden.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.danikula.android.garden.cache.DiscCache;
import com.danikula.android.garden.cache.LimitedMemoryAndDiscBitmapCache;
import com.danikula.android.garden.cache.SoftMemoryCache;
import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.CacheMemoryStorage;
import com.danikula.android.garden.utils.AndroidUtils;

import android.graphics.Bitmap;
import android.os.Environment;

public class StorageTest {

    @Test
    public void testWeakMemoryStorage() {
        runTestStorage(new SoftMemoryCache<String, String>());
    }

    @Test
    public void testStrongMemoryStorage() {
        runTestStorage(new CacheMemoryStorage<String, String>());
    }

    @Test
    public void testDiscStorage() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "cache");
        runTestStorage(new DiscCache(storageDir));
    }

    private void runTestStorage(Cache storage) {
        assertFalse(storage.contains("first"));

        storage.put("first", "one");
        assertTrue(storage.contains("first"));

        String value = (String) storage.get("first");
        assertEquals("one", value);

        storage.clear();
        assertFalse(storage.contains("first"));
    }

}
