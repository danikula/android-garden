package com.danikula.android.garden.content.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.danikula.android.garden.utils.Validate;

import android.database.Cursor;
import android.database.DatabaseUtils;

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
    public static <T> T getFirstObject(Cursor cursor, CursorConverter<T> converter) {
        Validate.notNull(converter, "converter");

        T entity = null;
        if (cursor != null) {
            cursor.moveToFirst();
            entity = converter.convert(cursor);
            cursor.close();
        }
        return entity;
    }

    public static <T> List<T> convertToListAndClose(Cursor cursor, CursorConverter<T> converter) {
        Validate.notNull(converter, "converter");
        List<T> list = new ArrayList<T>();
        iterateAndClose(cursor, new ObjectsCollector<T>(converter, list));
        return list;
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

    public String dumpAndClose(Cursor cursor) {
        try {
            return DatabaseUtils.dumpCursorToString(cursor);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static final class ObjectsCollector<T> implements IterateCursorHandler {

        private final CursorConverter<T> converter;
        private final List<T> list;

        private ObjectsCollector(CursorConverter<T> converter, List<T> list) {
            this.converter = converter;
            this.list = list;
        }

        @Override
        public void onRow(Cursor c) {
            list.add(converter.convert(c));
        }
    }

    private static final class IntValuesCollector implements IterateCursorHandler {

        private Set<Integer> values;
        private CursorConverter<Integer> converter;

        private IntValuesCollector(final String columnName, Set<Integer> values) {
            this.values = values;
            this.converter = new CursorConverter<Integer>() {

                @Override
                public Integer convert(Cursor cursor) {
                    return getInt(cursor, columnName);
                }
            };
        }

        @Override
        public void onRow(Cursor cursor) {
            Integer value = converter.convert(cursor);
            values.add(value);
        }
    }

}
