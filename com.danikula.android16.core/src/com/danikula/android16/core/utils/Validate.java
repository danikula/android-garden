package com.danikula.android16.core.utils;

public class Validate {
    
    public static void notNull(Object object, String objectName) {
        if (object == null) {
            throw new IllegalArgumentException(objectName + " can't be null");
        }
    }
    
    public static void checkTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

}
