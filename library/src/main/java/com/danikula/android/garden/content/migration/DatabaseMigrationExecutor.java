package com.danikula.android.garden.content.migration;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Executes migration for database.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class DatabaseMigrationExecutor implements MigrationExecutor {

    private static final String LOG_TAG = "DatabaseMigrationExecutor";

    private final SQLiteDatabase database;
    private final boolean logStatements;

    public DatabaseMigrationExecutor(SQLiteDatabase database, boolean logStatements) throws MigrationException {
        this.logStatements = logStatements;
        this.database = checkNotNull(database);
        if (database.isReadOnly()) {
            throw new MigrationException("Database must be writable!");
        }
    }

    @Override
    public void execute(List<String> statements) throws MigrationException {
        for (String statement : statements) {
            if (logStatements) {
                Log.i(LOG_TAG, "Execute migration statement: " + statement);
            }
            execute(statement);
        }
    }

    private void execute(String statement) throws MigrationException {
        try {
            database.execSQL(statement);
        } catch (SQLException e) {
            throw new MigrationException("Error execute sql statement: " + statement, e);
        }
    }
}
