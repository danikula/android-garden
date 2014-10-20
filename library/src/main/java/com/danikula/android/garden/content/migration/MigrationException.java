package com.danikula.android.garden.content.migration;

/**
 * Notifies about migrations problems.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class MigrationException extends Exception {

    public MigrationException() {
    }

    public MigrationException(String detailMessage) {
        super(detailMessage);
    }

    public MigrationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MigrationException(Throwable throwable) {
        super(throwable);
    }
}
