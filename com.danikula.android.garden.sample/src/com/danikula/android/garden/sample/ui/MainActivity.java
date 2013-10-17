package com.danikula.android.garden.sample.ui;

import java.util.LinkedList;
import java.util.List;

import com.danikula.aibolit.Aibolit;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.sample.ui.task.TaskDemoActivity;
import com.danikula.android.garden.ui.list.MutableListAdapter;
import com.danikula.android.garden.ui.list.ViewHolder;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.simple_list);

        List<ActivityItem> items = new LinkedList<MainActivity.ActivityItem>();
        items.add(new ActivityItem(TaskDemoActivity.class, "Long running task"));
        items.add(new ActivityItem(SectionedAdapterActivity.class, "Sectioned adapter"));

        ActivitiesAdapter adapter = new ActivitiesAdapter(this);
        adapter.setObjects(items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ActivitiesAdapter adapter = (ActivitiesAdapter) l.getAdapter();
        Class<? extends Activity> activityClass = adapter.getObject(position).getActivityClass();
        startActivity(new Intent(this, activityClass));
    }

    private static final class ActivitiesAdapter extends MutableListAdapter<ActivityItem> {

        public ActivitiesAdapter(Context context) {
            super(context, R.layout.simple_list_item, new int[] { android.R.id.text1 });
        }

        @Override
        protected void bind(ViewHolder viewHolder, ActivityItem item, int position) {
            viewHolder.setText(android.R.id.text1, item.getName());
        }
    }

    private static final class ActivityItem {

        private Class<? extends Activity> activityClass;

        private String name;

        private ActivityItem(Class<? extends Activity> activityClass, String name) {
            this.activityClass = activityClass;
            this.name = name;
        }

        public Class<? extends Activity> getActivityClass() {
            return activityClass;
        }

        public String getName() {
            return name;
        }
    }

}
