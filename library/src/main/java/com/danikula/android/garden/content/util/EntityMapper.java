package com.danikula.android.garden.content.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public abstract class EntityMapper<T> {
    
    public static final String DB_ID = BaseColumns._ID;
    
    private CursorReader cursorReader = new CursorReader();
    
    public abstract ContentValues toContentValues(T entity);
    
    public abstract T toEntity (Cursor cursor);

    protected String getString(Cursor cursor, String columnName) {
        return cursorReader.getString(cursor, columnName);
    }

    protected int getInt(Cursor cursor, String columnName) {
        return cursorReader.getInt(cursor, columnName);
    }

    protected long getLong(Cursor cursor, String columnName) {
        return cursorReader.getLong(cursor, columnName);
    }

    protected double getDouble(Cursor cursor, String columnName) {
        return cursorReader.getDouble(cursor, columnName);
    }

    protected boolean getBoolean(Cursor cursor, String columnName) {
        return cursorReader.getBoolean(cursor, columnName);
    }
}
