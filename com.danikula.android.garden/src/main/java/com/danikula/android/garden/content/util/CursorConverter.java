package com.danikula.android.garden.content.util;

import com.danikula.android.garden.utils.Converter;

import android.database.Cursor;

public abstract class CursorConverter<T> extends CursorReader implements Converter<Cursor, T> {

}
