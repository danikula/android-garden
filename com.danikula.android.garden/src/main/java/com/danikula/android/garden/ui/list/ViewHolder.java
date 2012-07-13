package com.danikula.android.garden.ui.list;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

    private View parent;

    private HashMap<Integer, View> views;

    public ViewHolder(View parent, int... ids) {
        this.views = new HashMap<Integer, View>();
        this.parent = parent;

        for (int id : ids) {
            View view = parent.findViewById(id);
            checkNotNull(view, "View vith id '0x%s' must be not null!", Integer.toHexString(id));
            views.put(id, view);
        }
    }

    public View getParent() {
        return parent;
    }

    public <V extends View> V findView(int id) {
        return (V) views.get(id);
    }

    public TextView findTextView(int textViewId) {
        return (TextView) findView(textViewId);
    }

    public ImageView findImageView(int imageViewId) {
        return (ImageView) findView(imageViewId);
    }

    public void setText(int textViewId, String text) {
        TextView textView = findViewAndCheckType(textViewId, TextView.class);
        textView.setText(text);
    }

    public void setShowParentContextMenuOnClickListener(int viewId) {
        View view = findViewAndCheckType(viewId, View.class);
        view.setOnClickListener(new PerformLongClickOnClickListener(parent));
    }

    private <V extends View> V findViewAndCheckType(int viewId, Class<? extends View> classView) {
        V view = findView(viewId);

        checkNotNull(view, "View with id '%s' must be not null!", viewId);
        checkState(classView.isInstance(view), "View with id '%s' is not %s", viewId, classView.getSimpleName());

        return view;
    }

}
