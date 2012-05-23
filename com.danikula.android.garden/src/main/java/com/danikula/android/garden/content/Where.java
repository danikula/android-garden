package com.danikula.android.garden.content;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;

public class Where {

    private static final String EQUALS = "%s = %s";

    private static final String IS_NULL = "%s IS NULL";

    private static final String AND = " AND ";

    private static final String OR = " OR ";

    private static final String IN = "%s IN (%s)";

    private StringBuilder condition = new StringBuilder();

    public Where and(Where where) {
        return and(where.build());
    }

    public Where and(String where) {
        return addCondition(AND, where);
    }

    public Where or(Where where) {
        return or(where.build());
    }

    public Where or(String where) {
        return addCondition(OR, where);
    }

    public Where addIsTrue(String where) {
        return and(where);
    }

    public Where addIsNull(String column) {
        checkNotNull(column, "Column must be not null!");

        return and(String.format(IS_NULL, column));
    }
    
    public Where addIn(String column, Iterable<?> iterable) {
        checkNotNull(column, "Column must be not null!");
        checkNotNull(iterable, "Iterable must be not null!");

        String set = Joiner.on(',').join(iterable);
        return and(String.format(IN, column, set));
    }

    public Where addEqual(String column, String value) {
        return isNullOrEqual(column, value == null ? null : String.format("'%s'", value));
    }

    public Where addEqual(String column, Integer value) {
        return isNullOrEqual(column, value);
    }

    public Where addEqual(String column, Long value) {
        return isNullOrEqual(column, value);
    }

    public Where addEqual(String column, Double value) {
        return isNullOrEqual(column, value);
    }

    public Where addEqual(String column, Boolean value) {
        return isNullOrEqual(column, value == null ? null : value ? 1 : 0);
    }

    private Where isNullOrEqual(String column, Object value) {
        checkNotNull(column, "Column must be not null!");

        return value == null ? addIsNull(column) : addIsTrue(String.format(EQUALS, column, String.valueOf(value)));
    }

    public String build() {
        return condition.toString();
    }

    public static String equal(String column, String value) {
        return new Where().addEqual(column, value).build();
    }

    public static String equal(String column, int value) {
        return new Where().addEqual(column, value).build();
    }

    public static String equal(String column, long value) {
        return new Where().addEqual(column, value).build();
    }

    public static String equal(String column, double value) {
        return new Where().addEqual(column, value).build();
    }

    public static String equal(String column, boolean value) {
        return new Where().addEqual(column, value).build();
    }

    public static String isNull(String column) {
        return new Where().addIsNull(column).build();
    }

    public static String in(String column, Iterable<? extends Number> iterable) {
        return new Where().addIn(column, iterable).build();
    }

    private Where addCondition(String operator, String where) {
        checkNotNull(where, "Where condition must be not null!");

        if (condition.length() != 0) {
            condition.append(operator);
        }

        condition.append(String.format("(%s)", where));
        return this;
    }
}
