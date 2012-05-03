package com.danikula.android.garden.content.util;

import java.util.HashMap;
import java.util.Map;

import com.danikula.android.garden.utils.Validate;

import android.database.Cursor;

public abstract class DefaultIterateCursorHandler implements IterateCursorHandler {
    
    private Map<String, Integer> columns = new HashMap<String, Integer>(); 

    protected String getString(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getString(columnIndex);
    }
    
    protected int getInt(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getInt(columnIndex);
    }
    
    protected double getDouble(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getDouble(columnIndex);
    }
    
    protected boolean getBoolean(Cursor cursor, String columnName) {
        int value = getInt(cursor, columnName);
        return value != 0;
    }

    private int getColumnIndex(Cursor cursor, String columnName) {
        Validate.notNull(cursor, "Cursor must be not null!");
        Validate.notNull(columnName, "Column's name must be not null!");
        
        int columnIndex = -1;
        if (columns.containsKey(columnName)) {
            columnIndex = columns.get(columnName);
        } else {
            columnIndex = cursor.getColumnIndexOrThrow(columnName);
            columns.put(columnName, columnIndex);
        }
        return columnIndex;
    }

}
