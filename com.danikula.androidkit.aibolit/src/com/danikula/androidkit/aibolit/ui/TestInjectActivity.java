package com.danikula.androidkit.aibolit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.Aibolit;
import com.danikula.androidkit.aibolit.R;
import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnItemClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnLongClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;
import com.danikula.androidkit.aibolit.annotation.InjectView;

public class TestInjectActivity extends Activity {

    @InjectView(R.id.textView)
    public TextView textView;

    @InjectView(R.id.listView)
    public ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Aibolit.doInjections(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    
    
    @InjectOnClickListener(R.id.button)
    private void onButtonClickListener(View view) {
        Log.d("debug", "onButtonClickListener!");
    }
    
    @InjectOnLongClickListener(R.id.button)
    private boolean onButtonLongClickListener(View view) {
        Log.d("debug", "onButtonLongClickListener! " + view);
        return true;
    }
    
    @InjectOnItemClickListener(R.id.listView)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("debug", String.format("onItemClick: %s, %s, %s, %s", parent, view, position, id));
    }
    
    @InjectOnTouchListener(R.id.textView)
    private boolean onTextViewTouch(View v, MotionEvent event){
        Log.d("debug", String.format("onTextViewTouch: %s, %s", v, event));
        return true;
    }
}