package com.danikula.android.garden.content.migration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Helps to migrate from one version to another.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class MigrationHelper {

    private static final String LOG_TAG = "MigrationHelper";

    public static void migrateToNewDatabaseVersion(Context context, SQLiteDatabase database, int from, int to, boolean logStatements) throws MigrationException {
        database.beginTransaction();
        try {
            MigrationExecutor migrationExecutor = new DatabaseMigrationExecutor(database, false, logStatements);
            for (int i = from; i < to; i++) {
                Log.i(LOG_TAG, "Execute database migration from " + i + " to " + (i + 1));
                MigrationSource migrationSource = new AssetsMigrationSource(context.getResources(), i, i + 1);
                Migration migration = new Migration(migrationSource, migrationExecutor);
                migration.execute();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
