package com.danikula.android.garden.test;

import junit.framework.TestCase;

public class TestStorage extends TestCase{
    
    public void testWeakMemoryStorage() {
        runTestStorage(new WeakMemoryStorage<String, String>());
    }
    
    public void testStrongMemoryStorage() {
        runTestStorage(new StrongMemoryStorage<String, String>());
    }
    
    public void testDiscStorage() {
        runTestStorage(new DiscStorage<String>("/sdcard/cache/"));
    }
    
    private void runTestStorage(Storage<String, String> storage) {
        assertFalse(storage.contains("first"));
        
        storage.put("first", "one");
        assertTrue(storage.contains("first"));
        
        String value = storage.get("first");
        assertEquals("one", value);
        
        storage.clear();
        assertFalse(storage.contains("first"));
    }
    
}
