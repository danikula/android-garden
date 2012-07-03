package com.danikula.android.garden.ui.list;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MutableListAdapter<T> extends BaseAdapter {

    private Context context;

    private List<T> objects = Lists.newArrayList();

    private int viewLayoutId;

    private int[] subviewIds;

    public MutableListAdapter(Context context, int viewLayoutId) {
        this(context, viewLayoutId, new int[0]);
    }

    public MutableListAdapter(Context context, int viewLayoutId, int[] subviewIds) {
        this.context = checkNotNull(context, "Context must be not null!");
        this.subviewIds = checkNotNull(subviewIds, "Subview ids must be not null!");
        this.viewLayoutId = viewLayoutId;
    }

    public void setObjects(List<T> objects) {
        this.objects = checkNotNull(objects, "Data must be not null!");
        super.notifyDataSetChanged();
    }

    public List<T> getObjects() {
        return objects;
    }

    public void addAll(List<T> newItems) {
        checkNotNull(newItems, "New data must be not null!");

        objects.addAll(newItems);
        setObjects(objects);
    }

    public T getObject(int position) {
        return objects.get(position);
    }

    public void clear() {
        setObjects(Collections.<T> emptyList());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? createView(position) : convertView;

        if (isViewBindable(position)) {
            bind(view, position);
        }

        return view;
    }

    protected View createView(int position) {
        return createViewWithViewHolder(viewLayoutId, subviewIds);
    }

    protected View createViewWithViewHolder(int layoutId, int[] subviewIds) {
        View view = inflateView(layoutId);
        view.setTag(new ViewHolder(view, subviewIds));
        return view;
    }

    protected View inflateView(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    protected void bind(View view, int position) {
        T object = getObject(position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        bind(viewHolder, object, position);
    }

    protected void bind(ViewHolder viewHolder, T object, int position) {
    }

    protected boolean isViewBindable(int position) {
        return true;
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public Object getItem(int position) {
        return getObject(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
