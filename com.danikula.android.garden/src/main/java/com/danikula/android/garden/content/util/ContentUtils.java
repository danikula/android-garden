package com.danikula.android.garden.content.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;

/**
 * Some useful methods for operating with content provider.
 * 
 * @author Alexey Danilov
 */
public class ContentUtils {

    /**
     * Converts first row from cursor to object if cursor isn't empty, otherwise returns <code>null</code>.
     * 
     * <p>
     * After reading object cursor will be closed
     * </p>
     * 
     * @param cursor Cursor cursor to read, can be <code>null</code>
     * @param mapper mapper to be used for converting cursor row to typed object
     * @return first row from cursor converted to object if cursor isn't empty, otherwise value will be empty.
     */
    public static <T> Optional<T> getFirst(Cursor cursor, EntityMapper<T> mapper) {
        checkNotNull(mapper, "Converter must be not null!");

        Optional<T> result = Optional.absent();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            T entity = mapper.toEntity(cursor);
            result = Optional.of(entity);
            cursor.close();
        }
        return result;
    }

    /**
     * Returns first single entity from storage or throw exception if there is no any entity in storage.
     * 
     * @param content content resolver, must be not {@code null}
     * @param uri uri to be used for retrieving entity, must be not {@code null}
     * @param where condition to be used for retrieving, must be not {@code null}
     * @param mapper mapper for converting cursor result to entity, must be not {@code null}
     * @return first result of retrieving
     * @throws IllegalArgumentException if any parameter is {@code null} or if there is no any row in storage
     */
    public static <T> T getFirstRequired(ContentResolver content, Uri uri, String where, EntityMapper<T> mapper) {
        checkNotNull(content, "Content resolver must be not null!");
        checkNotNull(uri, "Uri must be not null!");
        checkNotNull(where, "Condition must be not null!");
        checkNotNull(mapper, "Mapper must be not null!");

        Cursor сursor = content.query(uri, null, where, null, null);
        Optional<T> optionalScelet = getFirst(сursor, mapper);
        Preconditions.checkState(optionalScelet.isPresent(), String.format("There is no any entity with condition '%s'", where));
        return optionalScelet.get();
    }

    /**
     * Converts cursor to list with specified mapper. 
     * @param cursor cursor to be converted to list, can be {@code null}
     * @param converter converter to be used for converting cursor, can't be {@code null}
     * @return cursor converted to list
     */
    public static <T> List<T> convertToListAndClose(Cursor cursor, EntityMapper<T> converter) {
        checkNotNull(converter, "Converter must be not null!");

        List<T> list = Lists.newArrayList();
        iterateAndClose(cursor, new ObjectsCollector<T>(converter, list));
        return list;
    }

    public static Set<Integer> collectIntValues(Cursor cursor, String columnName) {
        Set<Integer> ids = Sets.newLinkedHashSet();
        iterateAndClose(cursor, new IntValuesCollector(columnName, ids));
        return ids;
    }

    private static void iterateAndClose(Cursor cursor, IterateCursorHandler handler) {
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

    public static ContentProviderOperation newInsert(Uri uri, ContentValues contentValues) {
        return ContentProviderOperation.newInsert(uri).withValues(contentValues).build();
    }

    public static ContentProviderOperation newDelete(Uri uri, String where) {
        return ContentProviderOperation.newDelete(uri).withSelection(where, null).build();
    }

    public static ContentProviderOperation newUpdate(Uri uri, ContentValues contentValues, String where) {
        return ContentProviderOperation.newUpdate(uri).withValues(contentValues).withSelection(where, null).build();
    }

    public static String dumpAndClose(Cursor cursor) {
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

        private final EntityMapper<T> mapper;
        private final List<T> list;

        private ObjectsCollector(EntityMapper<T> mapper, List<T> list) {
            this.mapper = mapper;
            this.list = list;
        }

        @Override
        public void onRow(Cursor c) {
            list.add(mapper.toEntity(c));
        }
    }

    private static final class IntValuesCollector implements IterateCursorHandler {

        private Set<Integer> values;
        private EntityMapper<Integer> converter;

        private IntValuesCollector(final String columnName, Set<Integer> values) {
            this.values = values;
            this.converter = new EntityMapper<Integer>() {

                @Override
                public Integer toEntity(Cursor cursor) {
                    return getInt(cursor, columnName);
                }

                @Override
                public ContentValues toContentValues(Integer entity) {
                    throw new UnsupportedOperationException("Should not be used!");
                }
            };
        }

        @Override
        public void onRow(Cursor cursor) {
            Integer value = converter.toEntity(cursor);
            values.add(value);
        }
    }

    private interface IterateCursorHandler {

        void onRow(Cursor cursor);

    }

}
