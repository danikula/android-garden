package com.danikula.android.garden.content;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Базовый класс для реализации своего контент провайдера.
 * <p>
 * Список фич:
 * <ul>
 * <li>
 * удобный способ регистрации типов сущностей с помощью метода {@link #registerEntity(String)}</li>
 * <li>дефолтовая реализация методов CRUD!</li>
 * <li>to be continued....</li>
 * </ul>
 * </p>
 * Пример реализации своего контент - провайдера:
 * 
 * <pre>
 * public class TasksContentProvider extends DatabaseContentProvider {
 * 
 *     private static final String CONTENT_AUTHORITY = &quot;com.ineed.taskprovider&quot;
 * 
 *     public TasksContentProvider() {
 *         super(CONTENT_AUTHORITY);
 *         registerEntity("task");
 *         registerEntity("user");
 *     }
 *     
 *     &#064;Override
 *     protected DatabaseHelper getDatabaseHelper() {
 *         return new TaskDatabaseHelper(getContext());
 *     }
 * }
 * </pre>
 * 
 * @author danik
 * 
 */
public abstract class DatabaseContentProvider extends ContentProvider {

    private static final String CONTENT_TYPE_ITEM_PATTERN = "vnd.android.cursor.item/vnd.%s.%s";
    private static final String CONTENT_TYPE_DIR_PATTERN = "vnd.android.cursor.dir/vnd.%s.%s";

    private Map<Integer, Item> contentTypeRegister = Maps.newHashMap();

    private DatabaseHelper dbHelper;

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private String authority;

    private int entityCounter;

    public DatabaseContentProvider(String authority) {
        this.authority = checkNotNull(authority, "Authority must be not null!");
    }

    @Override
    public boolean onCreate() {
        dbHelper = getDatabaseHelper();
        return dbHelper != null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * В качестве значения последней секции типа используется имя таблицы, где хранится сущность
     * </p>
     * */
    @Override
    public String getType(Uri uri) {
        int code = match(uri);
        checkCodeExist(code);
        Item item = contentTypeRegister.get(code);
        String pattern = item.isSingle() ? CONTENT_TYPE_ITEM_PATTERN : CONTENT_TYPE_DIR_PATTERN;
        return String.format(pattern, getContext().getPackageName(), item.getTable());
    }

    private void checkCodeExist(int code) {
        checkArgument(contentTypeRegister.containsKey(code), "Type with code '%s' is not registered!", code);
    }

    /** {@inheritDoc} */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SelectionBuilder builder = buildSelection(uri);
        Cursor cursor = builder.where(selection, selectionArgs).query(getReadableDb(), projection, sortOrder);
        // Register the contexts ContentResolver to be notified if the cursor result set changes.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /** {@inheritDoc} */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // some kind of name convention: type equals to table's name
        long rowId = insertInTable(uri, values);
        Uri newUri = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    private long insertInTable(Uri uri, ContentValues values) {
        String table = getTable(uri);
        return getDb().insertOrThrow(table, null, values);
    }

    /** {@inheritDoc} */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int inserted = values.length;
        try {
            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            for (ContentValues contentValues : values) {
                ContentProviderOperation insert = ContentProviderOperation.newInsert(uri).withValues(contentValues).build();
                operations.add(insert);
            }
            applyBatch(operations);
        }
        catch (OperationApplicationException e) {
            inserted = 0;
        }
        return inserted;
    }

    /** {@inheritDoc} */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = buildSelection(uri);
        int returnValue = builder.where(selection, selectionArgs).delete(getDb());
        getContext().getContentResolver().notifyChange(uri, null);
        return returnValue;
    }

    /** {@inheritDoc} */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = buildSelection(uri);
        int returnValue = builder.where(selection, selectionArgs).update(getDb(), values);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnValue;
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside a {@link SQLiteDatabase} transaction. All changes
     * will be rolled back if any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
        throws OperationApplicationException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested {@link Uri}. This is usually enough to support
     * {@link #insert}, {@link #update}, and {@link #delete} operations.
     */
    protected SelectionBuilder buildSelection(Uri uri) {
        int code = match(uri);
        checkCodeExist(code);
        Item item = contentTypeRegister.get(code);
        SelectionBuilder builder = new SelectionBuilder().table(item.getTable());
        if (item.isSingle()) {
            final String id = uri.getPathSegments().get(1);
            builder.table(item.getTable()).where(BaseColumns._ID + "=?", id);
        }
        return builder;
    }

    protected abstract DatabaseHelper getDatabaseHelper();

    protected SQLiteDatabase getDb() {
        checkDatabaseHelperExist();
        return dbHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDb() {
        checkDatabaseHelperExist();
        return dbHelper.getReadableDatabase();
    }

    protected int match(Uri uri) {
        return uriMatcher.match(uri);
    }

    /**
     * Регистрирует новую сущность.
     * 
     * @param entityName String имя сущности, должно совпадать с именем таблицы
     */
    protected void registerEntity(String entityName) {
        int singleEntityCode = ++entityCounter;
        contentTypeRegister.put(singleEntityCode, new Item(entityName, true));
        uriMatcher.addURI(authority, entityName + "/*", singleEntityCode);

        int multiEntityCode = ++entityCounter;
        contentTypeRegister.put(multiEntityCode, new Item(entityName, false));
        uriMatcher.addURI(authority, entityName, multiEntityCode);
    }

    /**
     * Регистрирует ресурс по указанному uri.
     * 
     * @param entityName имя сущности, должно совпадать с именем таблицы
     * @param path относительный путь uri
     * @param is <code>true</code> если ресурс представляет собой единичную сущность, <code>false</code> если список
     * @return code код зарегестрированного ресурса
     */
    protected int registerResource(String entityName, String path, boolean isSingle) {
        int code = ++entityCounter;
        contentTypeRegister.put(code, new Item(entityName, isSingle));
        uriMatcher.addURI(authority, path, code);
        return code;
    }

    /**
     * Возвращает имя таблицы, исходя из типа сущности
     * 
     * @param uri Uri uri, для которого необходимо определить таблицу, где хранится сущность
     * @return String имя таблицы
     * @see #getType(Uri)
     */
    private String getTable(Uri uri) {
        String type = getType(uri);
        return type.substring(type.lastIndexOf('.') + 1);
    }

    private void checkDatabaseHelperExist() {
        checkNotNull(dbHelper, "Database helper is not created. Be sure you are calling super.onCreate()");
    }

}
