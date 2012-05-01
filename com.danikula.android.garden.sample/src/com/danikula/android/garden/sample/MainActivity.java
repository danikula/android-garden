package com.danikula.android.garden.sample;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.danikula.aibolit.Aibolit;
import com.danikula.android.garden.ui.listadapter.MutableListAdapter;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.main);

        List<ActivityItem> items = new LinkedList<MainActivity.ActivityItem>();
        items.add(new ActivityItem(RemoteImageSampleActivity.class, "RemoteImageView"));
        items.add(new ActivityItem(ActionBarCompatSampleActivity.class, "ActionBar Compat"));

        ActivitiesAdapter adapter = new ActivitiesAdapter(this, android.R.layout.simple_list_item_1);
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

        public ActivitiesAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void bind(ActivityItem item, View view, int position) {
            ((TextView) view).setText(item.getName());
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
