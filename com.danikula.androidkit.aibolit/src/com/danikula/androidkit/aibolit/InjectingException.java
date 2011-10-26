package com.danikula.androidkit.aibolit;

public class InjectingException extends RuntimeException {

    private static final long serialVersionUID = -1L;
    
    public InjectingException(String message) {
        super(message);
    }

    public InjectingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectingException(Throwable cause) {
        super(cause);
    }
}
