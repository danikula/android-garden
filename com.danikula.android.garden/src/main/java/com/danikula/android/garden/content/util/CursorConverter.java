package com.danikula.android.garden.content.util;

import com.google.common.base.Function;

import android.database.Cursor;

public abstract class CursorConverter<T> extends CursorReader implements Function<Cursor, T> {

}
