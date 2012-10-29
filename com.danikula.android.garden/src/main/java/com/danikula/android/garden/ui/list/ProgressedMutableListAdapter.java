package com.danikula.android.garden.ui.list;

import java.util.List;

import com.danikula.android.garden.utils.ReflectUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class ProgressedMutableListAdapter<T> extends MutableListAdapter<T> {

    private static final int ROW_TYPE_LOADING = 0;
    private static final int ROW_TYPE_ITEM = 1;

    private static final int ROW_TYPES_COUNT = 2;

    private boolean showProgress = true;

    private int totalCount;

    private OnProgressShownListener onProgressShownListener;

    private int loadingViewId;

    private boolean isProgressAtBottom = true;

    public ProgressedMutableListAdapter(Context context, int layoutId, int[] subviewIds, int loadingViewId) {
        super(context, layoutId, subviewIds);
        this.loadingViewId = loadingViewId;
        setOnLoadingProgressShownListener(null);
    }

    public void setOnLoadingProgressShownListener(OnProgressShownListener listener) {
        OnProgressShownListener emptyListener = ReflectUtils.newInstance(OnProgressShownListener.class);
        this.onProgressShownListener = listener == null ? emptyListener : listener;
    }

    public void setProgressAtBottom(boolean isProgressAtBottom) {
        this.isProgressAtBottom = isProgressAtBottom;
    }

    public void setTotalCount(int totalResults) {
        this.totalCount = totalResults;
        checkProgress();
    }

    public int getTotalCount() {
        return totalCount;
    }

    private void checkProgress() {
        boolean shown = totalCount > super.getCount();
        if (shown != showProgress) {
            showProgress(shown);
        }
    }

    public void showProgress(boolean shown) {
        this.showProgress = shown;
        notifyDataSetChanged();
    }

    @Override
    public void setObjects(List<T> objects) {
        super.setObjects(objects);
        checkProgress();
    }

    @Override
    public void clear() {
        super.clear();
        checkProgress();
    }

    @Override
    public void addAll(List<T> newItems) {
        super.addAll(newItems);
        checkProgress();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLoadingView(position)) {
            boolean isLastPage = super.getCount() >= totalCount;
            boolean isOnlyProgressShown = super.getCount() == 0;
            onProgressShownListener.onListViewProgressShown(isLastPage, isOnlyProgressShown);
        }
        return super.getView(position, convertView, parent);
    }

    @Override
    protected View createView(int position) {
        int realPosition = isProgressAtBottom ? position : position - 1;
        return isLoadingView(position) ? inflateView(loadingViewId) : super.createView(realPosition);
    }

    @Override
    protected boolean isViewBindable(int position) {
        return !isLoadingView(position);
    }

    @Override
    public int getCount() {
        return showProgress ? super.getCount() + 1 : super.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return ROW_TYPES_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return isLoadingView(position) ? ROW_TYPE_ITEM : ROW_TYPE_LOADING;
    }

    protected boolean isLoadingView(int position) {
        return showProgress && (isProgressAtBottom ? position == getCount() - 1 : position == 0);
    }

    @Override
    public boolean isEnabled(int position) {
        return !isLoadingView(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isProgressShown() {
        return showProgress;
    }

    @Override
    public T getObject(int position) {
        int realPosition = !isProgressAtBottom && showProgress ? position - 1 : position;
        return super.getObject(realPosition);
    }

    public interface OnProgressShownListener {

        void onListViewProgressShown(boolean isLastPage, boolean isAdapterEmpty);
    }

}
