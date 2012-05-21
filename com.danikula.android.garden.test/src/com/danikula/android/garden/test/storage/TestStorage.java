package com.danikula.android.garden.test.storage;

import java.io.File;

import junit.framework.TestCase;

import com.danikula.android.garden.storage.DiscStorage;
import com.danikula.android.garden.storage.Storage;
import com.danikula.android.garden.storage.StrongMemoryStorage;
import com.danikula.android.garden.storage.SoftMemoryStorage;

import android.os.Environment;
import android.util.Log;

public class TestStorage extends TestCase{
    
    public void testWeakMemoryStorage() {
        runTestStorage(new SoftMemoryStorage<String, String>());
    }
    
    public void testStrongMemoryStorage() {
        runTestStorage(new StrongMemoryStorage<String, String>());
    }
    
    public void testDiscStorage() {
        String storagePath = new File(Environment.getExternalStorageDirectory(), "cache").getAbsolutePath();
        Log.d("debug", storagePath);
        Log.d("debug", Environment.getExternalStorageState());
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
