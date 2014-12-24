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

    /**
     * Executes migration from one version to another.
     * Note migration are not executed in transaction, so client HAVE TO run this method in transaction.
     *
     * @param context       android context
     * @param database      database to upgrade
     * @param from          version database will be upgraded from
     * @param to            version database will be upgraded to
     * @param logStatements whether migration should be logged
     * @throws MigrationException if any migration error occurs.
     */
    public static void migrateToNewDatabaseVersion(Context context, SQLiteDatabase database, int from, int to, boolean logStatements) throws MigrationException {
        MigrationExecutor migrationExecutor = new DatabaseMigrationExecutor(database, logStatements);
        for (int i = from; i < to; i++) {
            executeMigration(context, migrationExecutor, i);
        }
    }

    private static void executeMigration(Context context, MigrationExecutor migrationExecutor, int fromVersion) throws MigrationException {
        try {
            Log.i(LOG_TAG, "Execute database migration from " + fromVersion + " to " + (fromVersion + 1));
            MigrationSource migrationSource = new AssetsMigrationSource(context.getResources(), fromVersion, fromVersion + 1);
            Migration migration = new Migration(migrationSource, migrationExecutor);
            migration.execute();
        } catch (MigrationException e) {
            throw new MigrationException(e.getMessage(), e, fromVersion, fromVersion + 1);
        }
    }
}
