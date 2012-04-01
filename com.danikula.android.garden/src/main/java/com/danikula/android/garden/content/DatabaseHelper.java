package com.danikula.android.garden.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context, String database, int version) {
        super(context, database, null, version);
    }
    
    protected abstract String getCreateTablesSql();
    
    protected abstract String[] getAllTables();

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createTablesSql = getCreateTablesSql();
        String[] statements = createTablesSql.split(";");
        for (String statement : statements) {
            database.execSQL(statement);    
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        for (String table : getAllTables()) {
            database.execSQL("DROP TABLE IF EXISTS " + table);    
        }
        onCreate(database);
    }
}