package com.danikula.android.garden.content;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danikula.android.garden.io.IoUtils;

public abstract class RestoreBackupSQLiteOpenHelper extends SQLiteOpenHelper {

    private Context applicationContext;

    private String databaseName;

    private boolean isZipped;

    public RestoreBackupSQLiteOpenHelper(Context context, String database, int version, boolean isZipped) {
        super(context, database, null, version);
        this.applicationContext = context.getApplicationContext();
        this.databaseName = checkNotNull(database, "Database must not be null!");
        this.isZipped = isZipped;
        
        restore(false);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        restore(true);
    }

    private void restore(boolean replaceIfExisted) {
        File databaseFile = applicationContext.getDatabasePath(databaseName);
        boolean dbExist = databaseFile.exists();
        if (!dbExist || (dbExist && replaceIfExisted)) {
            restoreTo(databaseFile);
        }
    }

    private void restoreTo(File target) {
        InputStream sourceStream = null;
        OutputStream targetStream = null;
        try {
            createParentDirectoryIfNeeded(target);
            target.delete();

            InputStream sourceInputStream = applicationContext.getResources().getAssets().open(databaseName);
            if (isZipped) {
                sourceInputStream = new GZIPInputStream(sourceInputStream);
            }
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

    private void createParentDirectoryIfNeeded(File target) throws IOException {
        File parentDirectory = target.getParentFile();
        if (!parentDirectory.exists()) {
            boolean dirCreated = parentDirectory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Error creating directories for file " + target.getAbsolutePath());
            }
        }
    }
}