package com.danikula.android.garden.ui.listadapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.danikula.android.garden.utils.Validate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Useful adapter for {@link ExpandableListView}.
 * 
 * <p>
 * Adapter shows progres during loading data for groups and child rows.
 * </p>
 * @author Alexey Danilov
 *
 * @param <G> type of group items
 * @param <C> type of child items
 */
public abstract class ProgressedExpandableListAdapter<G, C> extends BaseExpandableListAdapter {

    private static final Object LOADING_ROW_MARKER = new Object();

    private List<G> groupItems;

    private List<List<C>> childItems;

    private LayoutInflater layoutInflater;

    private int groupLayoutId;

    private int childLayoutId;

    private int loadingLayoutId;

    public ProgressedExpandableListAdapter(Context context, int groupLayoutId, int childLayoutId, int loadingLayoutId) {
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        this.loadingLayoutId = loadingLayoutId;
        this.layoutInflater = LayoutInflater.from(context);
        this.groupItems = new ArrayList<G>();
        this.childItems = new ArrayList<List<C>>();
    }

    public void setGroupObjects(List<G> groupItems) {
        Validate.notNull(groupItems, "group items");
        this.groupItems.clear();
        this.groupItems.addAll(groupItems);

        childItems.clear();
        for (int i = 0; i < groupItems.size(); i++) {
            childItems.add(new ArrayList<C>());
        }

        notifyDataSetChanged();
    }
    
    public List<G> getGroupObjects() {
        int size = areGroupsLoading() ? getGroupCount() - 1 : getGroupCount();
        return new ArrayList<G>(groupItems.subList(0, size));
    }

    public void setChildItems(G group, List<C> newChildItems) {
        int groupIndex = findGroupIndex(group);
        List<C> childs = this.childItems.get(groupIndex);
        childs.clear();
        childs.addAll(newChildItems);
        notifyDataSetChanged();
    }

    public void setChildsLoading(int groupIndex, boolean loading) {
        List<C> childs = childItems.get(groupIndex);
        childs.clear();
        if (loading) {
         // loading marker
            childs.add(null);    
        }
        notifyDataSetChanged();
    }

    public void setGroupsLoading() {
        groupItems.clear();
        // loading marker
        groupItems.add(null);
        notifyDataSetChanged();
    }
    
    public boolean areGroupsLoading() {
        return getGroupCount() != 0 && isLoadingGroupView(0);
    }

    private int findGroupIndex(G searchingGroup) {
        for (int i = 0; i < groupItems.size(); i++) {
            G currentGroup = groupItems.get(i);
            if (areEquals(currentGroup, searchingGroup)) {
                return i;
            }
        }
        throw new IllegalArgumentException(String.format("There is no group item '%s'", searchingGroup));
    }

    protected abstract boolean areEquals(G currentGroup, G searchingGroup);

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView = createViewUsingConvertView(convertView, isLoadingGroupView(groupPosition), groupLayoutId);
        if (!isLoadingGroupView(groupPosition)) {
            G groupItem = groupItems.get(groupPosition);
            bindGroupView(groupView, groupItem, groupPosition, isExpanded);
        }
        return groupView;
    }

    protected abstract void bindGroupView(View groupView, G groupItem, int groupPosition, boolean isExpanded);

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        boolean isLoadingView = isLoadingChildView(groupPosition, childPosition);
        View childView = createViewUsingConvertView(convertView, isLoadingView, childLayoutId);
        if (!isLoadingChildView(groupPosition, childPosition)) {
            C child = (C) getChild(groupPosition, childPosition);
            bindChildView(childView, child, groupPosition, childPosition);
        }
        return childView;
    }

    protected abstract void bindChildView(View childView, C childItem, int groupPosition, int childPosition);

    private View createViewUsingConvertView(View convertView, boolean isLoadingView, int layoutId) {
        View reusableView = convertView;
        if (reusableView != null) {
            boolean isConvertViewLoadingRow = convertView.getTag() == LOADING_ROW_MARKER;
            boolean isCanReuseConvertView = isLoadingView == isConvertViewLoadingRow;
            reusableView = isCanReuseConvertView ? reusableView : null;
        }
        int inflatedLayoutId = isLoadingView ? loadingLayoutId : layoutId;
        View view = createView(reusableView, inflatedLayoutId);
        view.setTag(isLoadingView ? LOADING_ROW_MARKER : null);
        return view;
    }

    private View createView(View convertView, int layoutId) {
        return convertView != null ? convertView : layoutInflater.inflate(layoutId, null);
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return isLoadingGroupView(groupPosition) ? 0 : childItems.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItems.get(groupPosition);
    }
    
    public G getGroupItem(int groupPosition) {
        return (G) groupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItems.get(groupPosition).get(childPosition);
    }
    
    public C getChildItem(int groupPosition, int childPosition) {
        return (C) getChild(groupPosition, childPosition);
    }
    
    public List<C> getChilds(int groupPosition) {
        return Collections.unmodifiableList(childItems.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return !isLoadingChildView(groupPosition, childPosition);
    }
    
    public boolean isHasChildren(int groupIndex) {
        List<C> children = childItems.get(groupIndex);
        boolean isAloneChild = children.size() == 1;  
        return isAloneChild ? !isLoadingChildView(groupIndex, 0) : children.size() != 0;
    }

    private boolean isLoadingChildView(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition) == null;
    }

    private boolean isLoadingGroupView(int groupPosition) {
        return getGroup(groupPosition) == null;
    }

}