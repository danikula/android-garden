package com.danikula.android16.core.transport;

public class ResponseParsingException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public ResponseParsingException(String message) {
        super(message);
    }

    public ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseParsingException(Throwable cause) {
        super(cause);
    }
}