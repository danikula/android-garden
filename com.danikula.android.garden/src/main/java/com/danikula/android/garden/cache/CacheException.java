package com.danikula.android.garden.cache;

public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -1L;
    
    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
