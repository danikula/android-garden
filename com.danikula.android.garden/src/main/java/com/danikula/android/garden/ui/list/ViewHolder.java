package com.danikula.android.garden.ui.list;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

	private <V extends View> V findViewAndCheckType(int viewId, Class<? extends View> classView) {
		V view = findView(viewId);
		checkState(classView.isInstance(view), "View with id '%s' is not %s", viewId, classView.getSimpleName());
		return view;
	}

}
