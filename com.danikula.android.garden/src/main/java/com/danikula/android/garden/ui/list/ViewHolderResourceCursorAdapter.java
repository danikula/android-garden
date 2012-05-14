package com.danikula.android.garden.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;

public abstract class ViewHolderResourceCursorAdapter extends ResourceCursorAdapter {

    private int[] managedViewIds;

    public ViewHolderResourceCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery, int... managedViewIds) {
        super(context, layout, c, autoRequery);
        this.managedViewIds = managedViewIds;
    }

    public ViewHolderResourceCursorAdapter(Context context, int layout, Cursor c, int... managedViewIds) {
        super(context, layout, c);
        this.managedViewIds = managedViewIds;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView = super.newView(context, cursor, parent);
        newView.setTag(new ViewHolder(newView, managedViewIds));
        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        bindView(viewHolder, context, cursor);
    }

    public abstract void bindView(ViewHolder viewHolder, Context context, Cursor cursor);
}