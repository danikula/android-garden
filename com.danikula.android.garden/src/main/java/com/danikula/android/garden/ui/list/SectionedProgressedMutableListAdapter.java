package com.danikula.android.garden.ui.list;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import android.content.Context;
import android.util.Log;
import android.view.View;

public abstract class SectionedProgressedMutableListAdapter<G, T> extends ProgressedMutableListAdapter<T> {
    
    private static final String LOG_TAG = SectionedProgressedMutableListAdapter.class.getSimpleName();

    private static final int ROW_TYPE_GROUP = 2;

    private static final int ROW_TYPES_COUNT = 3;

    private Map<G, List<T>> groups = Maps.newLinkedHashMap();

    private Set<Integer> groupPositions = Sets.newLinkedHashSet();

    private int groupLayoutId;

    private int[] groupSubviewIds;

    private int viewCount;

    public SectionedProgressedMutableListAdapter(Context context, int layoutId, int[] subviewIds, int loadingViewId,
            int groupLayoutId, int[] groupSubviewIds) {
        super(context, layoutId, subviewIds, loadingViewId);
        this.groupSubviewIds = checkNotNull(groupSubviewIds, "Group subview ids must be not null!");
        this.groupLayoutId = groupLayoutId;
    }

    @Override
    public void setObjects(List<T> plainList) {
        super.setObjects(plainList);
        
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.setObjects. objects: " + plainList);

        groups = group(plainList);
        checkNotNull(groups, "Groups must be not null!");
        calculateGroupPositions();
        viewCount = plainList.size() + groups.size();

        notifyDataSetChanged();
    }

    private Map<G, List<T>> group(List<T> objects) {
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.group. objects: " + objects);
        
        Map<G, List<T>> groupedObjects = Maps.newLinkedHashMap();
        for (T object : objects) {
            G key = getGroupKey(object);
            List<T> group = groupedObjects.get(key);
            if (group == null) {
                group = Lists.newArrayList();
                groupedObjects.put(key, group);
            }
            group.add(object);
        }
        return groupedObjects;
    }

    private void calculateGroupPositions() {
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.calculateGroupPositions");
        
        groupPositions.clear();
        int offset = 0;
        int previousItemsCount = 0;
        for (List<T> group : groups.values()) {
            groupPositions.add(previousItemsCount + offset);
            offset++;
            previousItemsCount += group.size();
        }
    }

    @Override
    public T getObject(int position) {
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.getObject. position " + position);
        
        int objectIndex = getObjectIndex(position);
        return getObjects().get(objectIndex);
    }

    private int getObjectIndex(int rowPosition) {
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.getObjectIndex. position " + rowPosition);
        
        int groupIndex = 0;
        for (Integer groupPosition : groupPositions) {
            if (rowPosition < groupPosition) {
                return rowPosition - groupIndex;
            }
            groupIndex++;
        }
        if (groupIndex == groups.size()) {
            return rowPosition - groupIndex;
        }
        throw new IllegalArgumentException(String.format("Error getting item with position '%s'", rowPosition));
    }

    private G getGroup(int rowPosition) {
        int prevRowsCount = 0;
        for (G group : groups.keySet()) {
            List<T> groupItem = groups.get(group);
            prevRowsCount += groupItem.size() + 1;
            if (rowPosition <= prevRowsCount) {
                return group;
            }
        }
        throw new IllegalArgumentException(String.format("Error getting group object for item with position '%s'", rowPosition));
    }

    private boolean isGroup(int position) {
        return groupPositions.contains(position);
    }

    @Override
    public int getCount() {
        int count = isProgressShown() ? viewCount + 1 : viewCount;
        Log.d(LOG_TAG, "SectionedProgressedMutableListAdapter.getCount. count " + count);
        return count;
    }

    @Override
    public boolean isEnabled(int position) {
        return !isGroup(position) && super.isEnabled(position);
    }
    
    
    @Override
    public int getViewTypeCount() {
        return ROW_TYPES_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return isGroup(position) ? ROW_TYPE_GROUP : super.getItemViewType(position);
    }

    @Override
    protected View createView(int position) {
        return isGroup(position) ? createViewWithViewHolder(groupLayoutId, groupSubviewIds) : super.createView(position);
    }

    @Override
    protected void bind(View view, int position) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (isGroup(position)) {
            bindGroup(viewHolder, getGroup(position));
        }
        else {
            bindChild(viewHolder, getObject(position), position);
        }
    }

    protected abstract void bindChild(ViewHolder viewHolder, T object, int position);
    
    protected abstract G getGroupKey(T object);

    protected abstract void bindGroup(ViewHolder viewHolder, G groupObject);
}
