package com.danikula.android.garden.ui.list;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.danikula.android.garden.utils.UiUtils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ViewHolder {

    private View parent;

    private SparseArray<View> views;

    @Deprecated
    // for backward compatibility
    public ViewHolder(View parent, int... ids) {
        this(parent);
    }

    public ViewHolder(View parent) {
        this.views = new SparseArray<View>();
        this.parent = checkNotNull(parent, "Root view can't be null!");
    }

    public View getParent() {
        return parent;
    }

    public Context getContext() {
        return parent.getContext();
    }

    public <V extends View> V findView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = parent.findViewById(id);
            checkNotNull(view, "View vith id '0x%s' not exist!", Integer.toHexString(id));
            views.put(id, view);
        }
        return (V) view;
    }

    public TextView findTextView(int textViewId) {
        return (TextView) findView(textViewId);
    }

    public ImageView findImageView(int imageViewId) {
        return (ImageView) findView(imageViewId);
    }

    public void setText(int textViewId, CharSequence text) {
        TextView textView = findViewAndCheckType(textViewId, TextView.class);
        textView.setText(text);
    }

    public void setText(int textViewId, int textId) {
        TextView textView = findViewAndCheckType(textViewId, TextView.class);
        textView.setText(textId);
    }

    public void setTextAndHideIfEmpty(int textViewId, int textId) {
        setText(textViewId, textId);
        hideIfEmpty(textViewId);
    }

    public void setTextAndHideIfEmpty(int textViewId, CharSequence text) {
        setText(textViewId, text);
        hideIfEmpty(textViewId);
    }

    public void setShowParentContextMenuOnClickListener(int viewId) {
        View view = findViewAndCheckType(viewId, View.class);
        view.setOnClickListener(new PerformLongClickOnClickListener(parent));
    }

    public void resetTextViews(int... textViewIds) {
        for (int textViewId : textViewIds) {
            findTextView(textViewId).setText("");
        }
    }

    public void resetImageViews(int... imageViewIds) {
        for (int imageViewId : imageViewIds) {
            findImageView(imageViewId).setImageBitmap(null);
        }
    }

    public void setVisibility(boolean visible, int viewId) {
        View view = findView(viewId);
        UiUtils.setVisibility(visible, view);
    }

    public void setVisibilityGone(boolean visible, int viewId) {
        View view = findView(viewId);
        UiUtils.setVisibilityGone(visible, view);
    }

    public void hideIfEmpty(int... textViewIds) {
        for (int textViewId : textViewIds) {
            TextView view = findTextView(textViewId);
            boolean empty = TextUtils.isEmpty(view.getText());
            UiUtils.setVisibilityGone(!empty, view);
        }
    }

    private <V extends View> V findViewAndCheckType(int viewId, Class<? extends View> classView) {
        V view = findView(viewId);
        checkState(classView.isInstance(view), "View with id '%s' is not %s", viewId, classView.getSimpleName());
        return view;
    }

}
