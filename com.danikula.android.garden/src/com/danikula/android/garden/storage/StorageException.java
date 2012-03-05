package com.danikula.android.garden.storage;

public class StorageException extends RuntimeException {

    private static final long serialVersionUID = -1L;
    
    private int httpStatus;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
    
    public StorageException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public StorageException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public StorageException(Throwable cause, int httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }
    
    public int getHttpStatus() {
        return httpStatus;
    }
}
