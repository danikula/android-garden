package com.danikula.android.garden.content.util;

import android.database.Cursor;

public interface IterateCursorHandler {

    void onRow(Cursor cursor);

}