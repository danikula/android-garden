package com.danikula.android.garden.content;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;

/**
 * Класс для конструирования sql скрипта создания таблицы
 * 
 * @author Alexey Danilov
 * 
 */
public class TableBuilder {
    
    public enum Type {
        TEXT, INT, LONG, DOUBLE
    }

    private MultiTablesBuilder multiTablesBuilder;

    private static final String START = "create table '%s' (_id integer primary key autoincrement";

    private static final String END = ")";
    private StringBuilder sql;

    public TableBuilder(String table) {
        checkNotNull(table, "Table must be not null!");
        sql = new StringBuilder(String.format(START, table));
    }
    
    TableBuilder(MultiTablesBuilder multiTablesBuilder, String table) {
        this (table);
        this.multiTablesBuilder = checkNotNull(multiTablesBuilder, "Multi table builder must be not null!");
    }

    public TableBuilder column(String name, Type type, boolean required) {
        sql.append(String.format(", %s %s %s", name, type.toString(), required ? " NOT NULL" : ""));
        return this;
    }

    public TableBuilder columns(String[] columns, Type type, boolean required) {
        for (String column : columns) {
            column(column, type, required);
        }
        return this;
    }

    public TableBuilder intColumn(String name) {
        return column(name, Type.INT, false);
    }

    public TableBuilder intRequiredColumn(String name) {
        return column(name, Type.INT, true);
    }

    public TableBuilder intColumns(String... names) {
        return columns(names, Type.INT, false);
    }

    public TableBuilder intRequiredColumns(String... names) {
        return columns(names, Type.INT, true);
    }
    
    public TableBuilder textColumn(String name) {
        return column(name, Type.TEXT, false);
    }

    public TableBuilder textRequiredColumn(String name) {
        return column(name, Type.TEXT, true);
    }

    public TableBuilder textColumns(String... names) {
        return columns(names, Type.TEXT, false);
    }

    public TableBuilder textRequiredColumns(String... names) {
        return columns(names, Type.TEXT, true);
    }
    
    public TableBuilder longColumn(String name) {
        return column(name, Type.LONG, false);
    }

    public TableBuilder longRequiredColumn(String name) {
        return column(name, Type.LONG, true);
    }

    public TableBuilder longColumns(String... names) {
        return columns(names, Type.LONG, false);
    }

    public TableBuilder longRequiredColumns(String... names) {
        return columns(names, Type.LONG, true);
    }
    
    public TableBuilder doubleColumn(String name) {
        return column(name, Type.DOUBLE, false);
    }

    public TableBuilder doubleRequiredColumn(String name) {
        return column(name, Type.DOUBLE, true);
    }

    public TableBuilder doubleColumns(String... names) {
        return columns(names, Type.DOUBLE, false);
    }

    public TableBuilder doubleRequiredColumns(String... names) {
        return columns(names, Type.DOUBLE, true);
    }

    public String buildSql() {
        sql.append(END);
        return sql.toString();
    }
    
    public MultiTablesBuilder buildTable() {
        Preconditions.checkState(multiTablesBuilder != null, "Multi table builder is not passed in constructor!");
        return multiTablesBuilder.add(this);
    }

}
