package com.danikula.android.garden.content;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danikula.android.garden.io.IoUtils;

public abstract class RestoreBackupSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = RestoreBackupSQLiteOpenHelper.class.getSimpleName();

    public RestoreBackupSQLiteOpenHelper(Context context, String database, int version, int dbResourceId) {
        super(context, database, null, version);
        File databaseFile = context.getDatabasePath(database);
        boolean dbExist = databaseFile.exists();
        if (!dbExist || isNeedUpdate()) {
            copyDataBase(context, dbResourceId, databaseFile);
        }
    }

    private void copyDataBase(Context context, int rawSourceId, File target) {
        InputStream sourceStream = null;
        OutputStream targetStream = null;
        try {
            createParentDirectory(target);
            target.delete();

            InputStream sourceInputStream = context.getResources().openRawResource(rawSourceId);
            sourceStream = new BufferedInputStream(sourceInputStream);
            targetStream = new BufferedOutputStream(new FileOutputStream(target));
            IoUtils.copy(sourceStream, targetStream);
            targetStream.flush();
        }
        catch (IOException e) {
            throw new IllegalStateException("Error copying database file " + target.getAbsolutePath(), e);
        }
        finally {
            IoUtils.closeSilently(sourceStream);
            IoUtils.closeSilently(targetStream);
        }
    }

    private void createParentDirectory(File target) throws IOException {
        boolean dirCreated = target.getParentFile().mkdirs();
        if (!dirCreated) {
            throw new IOException("Error creating directories for file " + target.getAbsolutePath());
        }
    }

    protected boolean isNeedUpdate() {
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}