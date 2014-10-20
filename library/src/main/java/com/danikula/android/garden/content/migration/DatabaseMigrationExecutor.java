package com.danikula.android.garden.content.migration;

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
    private final boolean useTransaction;
    private final boolean logStatements;

    public DatabaseMigrationExecutor(SQLiteDatabase database, boolean useTransaction, boolean logStatements) throws MigrationException {
        this.useTransaction = useTransaction;
        this.logStatements = logStatements;
        this.database = checkNotNull(database);
        if (database.isReadOnly()) {
            throw new MigrationException("Database must be writable!");
        }
    }

    @Override
    public void execute(List<String> statements) throws MigrationException {
        if (useTransaction) {
            executeInTransaction(statements);
        } else {
            executeWithoutTransaction(statements);
        }
    }

    private void executeInTransaction(List<String> statements) {
        try {
            database.beginTransaction();
            executeWithoutTransaction(statements);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void executeWithoutTransaction(List<String> statements) {
        for (String statement : statements) {
            if (logStatements) {
                Log.i(LOG_TAG, "Execute migration statement: " + statement);
            }
            database.execSQL(statement);
        }
    }
}
