package com.danikula.android.garden.ui.list;

import java.util.HashMap;

import com.danikula.android.garden.utils.Validate;

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
            Validate.notNull(view, "view with id " + id);
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

        Validate.notNull(view, "View with id " + viewId);
        Validate.checkTrue(classView.isInstance(view),
                String.format("View with id '%s' is not %s", viewId, classView.getSimpleName()));

        return view;
    }

}
