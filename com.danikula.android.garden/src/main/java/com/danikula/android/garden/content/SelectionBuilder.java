/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Modifications:
 * -Imported from AOSP frameworks/base/core/java/com/android/internal/content
 * -Changed package name
 */

package com.danikula.android.garden.content;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Helper for building selection clauses for {@link SQLiteDatabase}. Each appended clause is combined using {@code AND}. This
 * class is <em>not</em> thread safe.
 */
public class SelectionBuilder {
    private String table = null;
    private Map<String, String> projectionMap = new HashMap<String, String>();
    private StringBuilder selection = new StringBuilder();
    private ArrayList<String> selectionArgs = new ArrayList<String>();

    /**
     * Reset any internal state, allowing this builder to be recycled.
     */
    public SelectionBuilder reset() {
        table = null;
        selection.setLength(0);
        selectionArgs.clear();
        return this;
    }

    /**
     * Append the given selection clause to the internal state. Each clause is surrounded with parenthesis and combined using
     * {@code AND}.
     */
    public SelectionBuilder where(String whereSelection, String... whereSelectionArgs) {
        if (TextUtils.isEmpty(whereSelection)) {
            checkState(!(whereSelectionArgs != null && whereSelectionArgs.length > 0),
                    "Valid selection required when including arguments=");
            // Shortcut when clause is empty
            return this;
        }

        if (selection.length() > 0) {
            selection.append(" AND ");
        }

        selection.append("(").append(whereSelection).append(")");
        if (whereSelectionArgs != null) {
            for (String arg : whereSelectionArgs) {
                selectionArgs.add(arg);
            }
        }

        return this;
    }

    public SelectionBuilder table(String t) {
        this.table = t;
        return this;
    }

    private void assertTable() {
        checkNotNull(table, "Table not specified");
    }

    public SelectionBuilder mapToTable(String column, String table) {
        projectionMap.put(column, table + "." + column);
        return this;
    }

    public SelectionBuilder map(String fromColumn, String toClause) {
        projectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    /**
     * Return selection string for current internal state.
     * 
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        return selection.toString();
    }

    /**
     * Return selection arguments for current internal state.
     * 
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    private void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = projectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy, String having, String orderBy, String limit) {
        assertTable();
        if (columns != null) {
            mapColumns(columns);
        }
        return db.query(table, columns, getSelection(), getSelectionArgs(), groupBy, having, orderBy, limit);
    }

    /**
     * Execute update using the current internal state as {@code WHERE} clause.
     */
    public int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        return db.update(table, values, getSelection(), getSelectionArgs());
    }

    /**
     * Execute delete using the current internal state as {@code WHERE} clause.
     */
    public int delete(SQLiteDatabase db) {
        assertTable();
        return db.delete(table, getSelection(), getSelectionArgs());
    }
}
