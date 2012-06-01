package com.danikula.android.garden.content.util;

import static com.google.common.base.Preconditions.checkNotNull;

import android.database.Cursor;

public class CursorReader {

    public String getString(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getString(columnIndex);
    }

    public int getInt(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getInt(columnIndex);
    }

    public long getLong(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getLong(columnIndex);
    }

    public double getDouble(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getDouble(columnIndex);
    }

    public boolean getBoolean(Cursor cursor, String columnName) {
        int value = getInt(cursor, columnName);
        return value != 0;
    }

    private int getColumnIndex(Cursor cursor, String columnName) {
        checkNotNull(cursor, "Cursor must be not null!");
        checkNotNull(columnName, "Column's name must be not null!");

        return cursor.getColumnIndexOrThrow(columnName);
    }

}
