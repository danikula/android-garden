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
    
    public TableCreatorBuilder intColumn(String name, boolean required) {
        return column(name, Type.INT, required);
    }

    public TableCreatorBuilder requiredIntColumn(String name) {
        return intColumn(name, true);
    }

    public TableCreatorBuilder notRequiredIntColumn(String name) {
        return intColumn(name, false);
    }

    public TableCreatorBuilder textColumn(String name, boolean required) {
        return column(name, Type.TEXT, required);
    }

    public TableCreatorBuilder requiredTextColumn(String name) {
        return textColumn(name, true);
    }

    public TableCreatorBuilder notRequiredTextColumn(String name) {
        return textColumn(name, false);
    }

    public TableCreatorBuilder longColumn(String name, boolean required) {
        return column(name, Type.LONG, required);
    }

    public TableCreatorBuilder requiredLongColumn(String name) {
        return longColumn(name, true);
    }

    public TableCreatorBuilder notRequiredLongColumn(String name) {
        return longColumn(name, false);
    }

    public TableCreatorBuilder doubleColumn(String name, boolean required) {
        return column(name, Type.DOUBLE, required);
    }

    public TableCreatorBuilder requiredDoubleColumn(String name) {
        return doubleColumn(name, true);
    }

    public TableCreatorBuilder notRequiredDoubleColumn(String name) {
        return doubleColumn(name, false);
    }

    public String build() {
        sql.append(END);
        return sql.toString();
    }

}
