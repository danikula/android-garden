package com.danikula.android.garden.sample.ui;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.http.params.HttpAbstractParamBean;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.ui.list.SectionedProgressedMutableListAdapter;
import com.danikula.android.garden.ui.list.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SectionedAdapterActivity extends Activity {

    @ViewById(android.R.id.list)
    private ListView listView;
    
    private TimeZonesAdapter adapter;
    
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.sectioned_adapter_activity);
        adapter = new TimeZonesAdapter(this);
        listView.setAdapter(adapter);
    }
    
    @OnClick(R.id.loadButton)
    private void onLoadingButtonCLick(View view) {
        adapter.showProgress(true);
        handler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                adapter.showProgress(false);
                adapter.setObjects(Arrays.asList(TimeZone.getAvailableIDs()));                
            }
        }, 2000);
    }

    private class TimeZonesAdapter extends SectionedProgressedMutableListAdapter<String, String> {

        public TimeZonesAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, new int[] { android.R.id.text1 }, R.layout.loading_list_item,
                    R.layout.group_list_item, new int[0]);
        }

        @Override
        protected String getGroupKey(String timeZone) {
            return timeZone.substring(0, 1);
        }

        @Override
        protected void bindChild(ViewHolder viewHolder, String object, int position) {
            viewHolder.setText(android.R.id.text1, object);
        }

        @Override
        protected void bindGroup(ViewHolder viewHolder, String groupObject) {
            ((TextView)viewHolder.getParent()).setText(groupObject);
        }

    }

}
