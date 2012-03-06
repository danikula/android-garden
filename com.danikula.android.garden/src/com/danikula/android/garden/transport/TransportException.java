package com.danikula.android.garden.transport;

public class TransportException extends Exception {

    private static final long serialVersionUID = -1L;

    private int httpStatus;

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransportException(Throwable cause) {
        super(cause);
    }

    public TransportException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public TransportException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public TransportException(Throwable cause, int httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
