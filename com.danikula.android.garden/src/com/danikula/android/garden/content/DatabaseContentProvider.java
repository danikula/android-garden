package com.danikula.android.garden.content;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Базовый класс для реализации своего контент провайдера.
 * <p>
 * Список фич:
 * <ul>
 * <li>
 * удобный способ регистрации типов сущеностей с помощью методов {@link #registerMultipleItem(int, String)}
 * {@link #registerSingleItem(int, String, String)} и как следствие реализованный метод {@link #getType(Uri)}</li>
 * <li>дефолтовая реализация методов CRUD!</li>
 * <li>to be continued....</li>
 * </ul>
 * </p>
 * Пример реализации своего контент - провайдера:
 * 
 * <pre>
 * public class TasksContentProvider extends DatabaseContentProvider {
 * 
 *     private static final String CONTENT_AUTHORITY = &quot;com.ineed.taskprovider&quot;;
 *     static final Uri BASE_CONTENT_URI = Uri.parse(&quot;content://&quot; + CONTENT_AUTHORITY);
 * 
 *     private static final int TASKS = 100;
 *     private static final int TASK_ITEM = 101;
 * 
 *     public TasksContentProvider() {
 *         addMathURI(CONTENT_AUTHORITY, TaskItem.PATH, TASKS);
 *         addMathURI(CONTENT_AUTHORITY, TaskItem.PATH + &quot;/*&quot;, TASK_ITEM);
 * 
 *         registerMultipleItem(TASKS, Tables.TASK);
 *         registerSingleItem(TASK_ITEM, Tables.TASK, TaskItem._ID);
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

    private Map<Integer, Item> contentTypeRegister = new HashMap<Integer, Item>();

    private DatabaseHelper dbHelper;

    private UriMatcher uriMathcer = new UriMatcher(UriMatcher.NO_MATCH);

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
        if (!contentTypeRegister.containsKey(code)) {
            throw new IllegalArgumentException(String.format("Type with code '%s' is not registered!. "
                    + "Use method 'registerContentType' for registering a new content type", code));
        }
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
        for (int i = 0; i < values.length; i++) {
            insertInTable(uri, values[i]);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return values.length;
    }

    /** {@inheritDoc} */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = buildSelection(uri);
        int retVal = builder.where(selection, selectionArgs).delete(getDb());
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = buildSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(getDb(), values);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
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
            builder.table(item.getTable()).where(item.getIdPropertyName() + "=?", id);
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
    
    /**
     * Регистрирует новую сущности.
     * 
     * @param code int идентификтор сущности, возвращаемые для данной сущности методом {@link #match(Uri)}
     * @param table String имя таблицы, в которой хранится сущность
     */
    protected void registerSingleItem(int code, String table, String propertyName) {
        contentTypeRegister.put(code, new Item(table, propertyName, true));
    }

    /**
     * Регистрирует новую сущности.
     * 
     * @param code int идентификтор сущности, возвращаемые для данной сущности методом {@link #match(Uri)}
     * @param table String имя таблицы, в которой хранится сущность
     */
    protected void registerMultipleItem(int code, String table) {
        contentTypeRegister.put(code, new Item(table, null, false));
    }

    protected int match(Uri uri) {
        return uriMathcer.match(uri);
    }

    protected void addMathURI(String authority, String path, int code) {
        uriMathcer.addURI(authority, path, code);
    }
    
    /**
     * Возвращает имя таблицы, исходя из типа сущности
     * @param uri Uri uri, для которого необходимо определить таблицу, где хранится сущность
     * @return String имя таблицы
     * @see #getType(Uri)
     */
    private String getTable(Uri uri) {
        String type = getType(uri);
        return type.substring(type.lastIndexOf('.') + 1);
    }

    private void checkDatabaseHelperExist() {
        if (dbHelper == null) {
            throw new IllegalArgumentException("Database helper is not created. Be sure you are calling super.onCreate()");
        }
    }

}
