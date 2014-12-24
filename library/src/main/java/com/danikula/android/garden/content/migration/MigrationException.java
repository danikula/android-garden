package com.danikula.android.garden.content.migration;

/**
 * Notifies about migrations problems.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class MigrationException extends Exception {

    public final int fromVersion;
    public final int toVersion;

    public MigrationException() {
        this("");
    }

    public MigrationException(String detailMessage) {
        this(detailMessage, null);
    }

    public MigrationException(Throwable throwable) {
        this(null, throwable);
    }

    public MigrationException(String detailMessage, Throwable throwable) {
        this(detailMessage, throwable, 0, 0);
    }

    public MigrationException(String detailMessage, Throwable throwable, int from, int to) {
        super(detailMessage, throwable);
        this.fromVersion = from;
        this.toVersion = to;
    }
}
