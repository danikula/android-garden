package com.danikula.android.garden.content;

/**
 * Класс для конструирования sql скрипта создания таблицы
 * 
 * @author Alexey Danilov
 * 
 */
public class TableCreatorBuilder {

    public enum Type {
        TEXT, INT, LONG, DOUBLE
    }

    private static final String START = "create table '%s' (_id integer primary key autoincrement";

    private static final String END = ")";
    private StringBuilder sql;

    public TableCreatorBuilder(String tableName) {
        sql = new StringBuilder(String.format(START, tableName));
    }

    public TableCreatorBuilder column(String name, Type type, boolean required) {
        sql.append(String.format(", %s %s %s", name, type.toString(), required ? " NOT NULL" : ""));
        return this;
    }

    public TableCreatorBuilder columns(String[] columns, Type type, boolean required) {
        for (String column : columns) {
            column(column, type, required);
        }
        return this;
    }

    public TableCreatorBuilder intColumn(String name) {
        return column(name, Type.INT, false);
    }

    public TableCreatorBuilder intRequiredColumn(String name) {
        return column(name, Type.INT, true);
    }

    public TableCreatorBuilder intColumns(String... names) {
        return columns(names, Type.INT, false);
    }

    public TableCreatorBuilder intRequiredColumns(String... names) {
        return columns(names, Type.INT, true);
    }
    
    public TableCreatorBuilder textColumn(String name) {
        return column(name, Type.TEXT, false);
    }

    public TableCreatorBuilder textRequiredColumn(String name) {
        return column(name, Type.TEXT, true);
    }

    public TableCreatorBuilder textColumns(String... names) {
        return columns(names, Type.TEXT, false);
    }

    public TableCreatorBuilder textRequiredColumns(String... names) {
        return columns(names, Type.TEXT, true);
    }
    
    public TableCreatorBuilder longColumn(String name) {
        return column(name, Type.LONG, false);
    }

    public TableCreatorBuilder longRequiredColumn(String name) {
        return column(name, Type.LONG, true);
    }

    public TableCreatorBuilder longColumns(String... names) {
        return columns(names, Type.LONG, false);
    }

    public TableCreatorBuilder longRequiredColumns(String... names) {
        return columns(names, Type.LONG, true);
    }
    
    public TableCreatorBuilder doubleColumn(String name) {
        return column(name, Type.DOUBLE, false);
    }

    public TableCreatorBuilder doubleRequiredColumn(String name) {
        return column(name, Type.DOUBLE, true);
    }

    public TableCreatorBuilder doubleColumns(String... names) {
        return columns(names, Type.DOUBLE, false);
    }

    public TableCreatorBuilder doubleRequiredColumns(String... names) {
        return columns(names, Type.DOUBLE, true);
    }

    public String build() {
        sql.append(END);
        return sql.toString();
    }

}
