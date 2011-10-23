package com.danikula.android16.core.ui.support;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.danikula.android16.core.utils.Validate;

public abstract class MutableListAdapter<T> extends BaseAdapter {

    private static final int UNDEFINED_LAYOUT_ID = -1;

    private Context context;

    private int layoutId;

    private List<T> objects = new ArrayList<T>();

    // public MutableListAdapter(Context context) {
    // this(context, UNDEFINED_LAYOUT_ID);
    // }

    public MutableListAdapter(Context context, int layoutId) {
        Validate.notNull(context, "Context");
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = createView(context, position);
        }

        if (isViewBindable(position)) {
            T object = getObject(position);

            bind(object, view, position);
        }

        return view;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return getObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setObjects(List<T> objects) {
        this.objects = new ArrayList<T>(objects);
        super.notifyDataSetChanged();
    }

    public void addObjects(List<T> newItems) {
        if (objects == null) {
            objects = new ArrayList<T>(objects);
        }
        objects.addAll(newItems);
        super.notifyDataSetChanged();
    }

    public void addObject(T newItems) {
        if (objects == null) {
            objects = new ArrayList<T>(objects);
        }
        objects.add(newItems);
        super.notifyDataSetChanged();
    }

    public void addObjectTo(T newItems, int index) {
        if (objects == null) {
            objects = new ArrayList<T>(objects);
        }
        objects.add(0, newItems);
        super.notifyDataSetChanged();
    }

    public T getObject(int position) {
        return objects.get(position);
    }

    public void resetData() {
        setObjects(new ArrayList<T>());
    }

    protected Context getContext() {
        return context;
    }

    protected View createView(Context context, int position) {
        if (layoutId == UNDEFINED_LAYOUT_ID) {
            throw new IllegalStateException("Layout identifier for row view is not specified!");
        }
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    protected abstract void bind(T object, View view, int position);

    protected boolean isViewBindable(int position) {
        return true;
    }
}
