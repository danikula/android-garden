package com.danikula.android.garden.content.migration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simple string source for {@link Migration}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class StringMigrationSource implements MigrationSource {

    private final String statements;

    public StringMigrationSource(String statements) {
        this.statements = checkNotNull(statements);
    }

    @Override
    public InputStream open() throws MigrationException {
        return new ByteArrayInputStream(statements.getBytes());
    }
}