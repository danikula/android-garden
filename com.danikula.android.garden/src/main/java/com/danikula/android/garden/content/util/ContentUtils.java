package com.danikula.android.garden.content.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.danikula.android.garden.utils.Converter;
import com.danikula.android.garden.utils.Validate;

import android.database.Cursor;

/**
 * Some useful methods for operating with content provider.
 * 
 * @author Alexey Danilov
 */
public class ContentUtils {

    public static void iterateAndClose(Cursor cursor, IterateCursorHandler handler) {
        if (cursor == null) {
            return;
        }

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            handler.onRow(cursor);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public static <T> List<T> convertToListAndClose(Cursor cursor, final Converter<Cursor, T> converter) {
        final List<T> list = new ArrayList<T>();
        iterateAndClose(cursor, new DefaultIterateCursorHandler() {

            @Override
            public void onRow(Cursor c) {
                list.add(converter.convert(c));
            }
        });
        return list;
    }

    /**
     * Converts first row from cursor to object if cursor isn't empty, otherwise returns <code>null</code>.
     * 
     * <p>
     * After reading object cursor will be closed
     * </p>
     * 
     * @param cursor Cursor cursor to read, can be <code>null</code>
     * @param converter converter to be used for converting cursor row to typed object
     * @return first row from cursor converted to object if cursor isn't empty, or <code>null</code> otherwise.
     */
    public static <T> T getFirstObject(Cursor cursor, Converter<Cursor, T> converter) {
        Validate.notNull(converter, "converter");
        
        T entity = null;
        if (cursor != null) {
            cursor.moveToFirst();
            entity = converter.convert(cursor);
            cursor.close();
        }
        return entity;
    }

    public static Set<Integer> collectIntValues(Cursor cursor, String columnName) {
        Set<Integer> ids = new LinkedHashSet<Integer>();
        iterateAndClose(cursor, new IntValuesCollector(columnName, ids));
        return ids;
    }

    public static List<Long> collectIntValuesInList(Cursor cursor, String columnName) {
        return new ArrayList(collectIntValues(cursor, columnName));
    }

    public static String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getString(columnIndex);
    }

    public static int getInt(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getInt(columnIndex);
    }

    public static Double getDouble(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        return cursor.getDouble(columnIndex);
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        int value = getInt(cursor, columnName);
        return value != 0;
    }

    private static final class IntValuesCollector extends DefaultIterateCursorHandler {

        private Set<Integer> values;
        private final String columnName;

        private IntValuesCollector(String columnName, Set<Integer> values) {
            this.columnName = columnName;
            this.values = values;
        }

        @Override
        public void onRow(Cursor cursor) {
            values.add(getInt(cursor, columnName));
        }
    }

}
