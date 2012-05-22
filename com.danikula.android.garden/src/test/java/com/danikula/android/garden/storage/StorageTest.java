package com.danikula.android.garden.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import android.os.Environment;

public class StorageTest {

    @Test
    public void testWeakMemoryStorage() {
        runTestStorage(new SoftMemoryStorage<String, String>());
    }

    @Test
    public void testStrongMemoryStorage() {
        runTestStorage(new StrongMemoryStorage<String, String>());
    }

    @Test
    public void testDiscStorage() {
        String storagePath = new File(Environment.getExternalStorageDirectory(), "cache").getAbsolutePath();
        runTestStorage(new DiscStorage(storagePath));
    }

    private void runTestStorage(Storage storage) {
        assertFalse(storage.contains("first"));

        storage.put("first", "one");
        assertTrue(storage.contains("first"));

        String value = (String) storage.get("first");
        assertEquals("one", value);

        storage.clear();
        assertFalse(storage.contains("first"));
    }

}
