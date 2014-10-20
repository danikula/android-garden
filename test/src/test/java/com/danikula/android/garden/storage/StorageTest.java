package com.danikula.android.garden.storage;

import android.os.Environment;
import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.CacheMemoryStorage;
import com.danikula.android.garden.cache.DiscCache;
import com.danikula.android.garden.cache.SoftMemoryCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
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
