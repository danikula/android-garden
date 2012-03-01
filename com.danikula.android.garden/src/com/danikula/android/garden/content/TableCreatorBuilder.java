package com.danikula.android.garden.content;

/**
 * Класс для конструирования sql скрипта создания таблицы
 * 
 * @author danik
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

    public String build() {
        sql.append(END);
        return sql.toString();
    }

}
