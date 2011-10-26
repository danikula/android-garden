package com.danikula.androidkit.aibolit.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.Aibolit;
import com.danikula.androidkit.aibolit.InjectionResolver;
import com.danikula.androidkit.aibolit.R;
import com.danikula.androidkit.aibolit.annotation.InjectOnCheckedChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnFocusChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnItemClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnKeyListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnLongClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnRadioGroupCheckedChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTextChangedListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;
import com.danikula.androidkit.aibolit.annotation.InjectService;
import com.danikula.androidkit.aibolit.annotation.InjectSystemService;
import com.danikula.androidkit.aibolit.annotation.InjectView;

public class TestInjectActivity extends Activity {

    private SimpleService simpleService = new SimpleService();

    @InjectService
    private SimpleService injectedService;

    @InjectView(R.id.textView)
    public TextView textView;

    @InjectView(R.id.listView)
    public ListView listView;

    @InjectSystemService(Context.LAYOUT_INFLATER_SERVICE)
    private LayoutInflater layoutInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.addInjectionResolver(new CustomServiceResolver());

        Aibolit.setContentView(this, R.layout.main);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        View inflatedView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        Log.d("debug", "inflated view: " + inflatedView);

        Log.d("debug", "injected service: " + injectedService);
    }

    @InjectOnClickListener(R.id.button)
    private void onButtonClickListener(View view) {
        Log.d("debug", "onButtonClickListener!");
    }

    @InjectOnLongClickListener(R.id.button)
    private boolean onButtonLongClickListener(View view) {
        Log.d("debug", "onButtonLongClickListener! " + view);
        return false;
    }

    @InjectOnItemClickListener(R.id.listView)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("debug", String.format("onItemClick: %s, %s, %s, %s", parent, view, position, id));
    }

    @InjectOnTouchListener(R.id.textView)
    private boolean onTextViewTouch(View v, MotionEvent event) {
        Log.d("debug", String.format("onTextViewTouch: %s, %s", v, event));
        return false;
    }

    @InjectOnFocusChangeListener(R.id.editText)
    private void onFocusChange(View v, boolean hasFocus) {
        Log.d("debug", String.format("onFocusChange: %s, %s", v, hasFocus));
    }

    @InjectOnTextChangedListener(R.id.editText)
    private void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("debug", String.format("onTextChanged: %s, %s, %s, %s", s, start, before, count));
    }

    @InjectOnCheckedChangeListener(R.id.checkbox)
    private void onCheckedChanged(android.widget.CompoundButton arg0, boolean arg1) {
        Log.d("debug", String.format("onCheckedChanged: %s, %s", arg0, arg1));
    }
    
    @InjectOnRadioGroupCheckedChangeListener(R.id.radiogroup)
    private void onRadioGroupCheckedChanged(RadioGroup rg, int arg1) {
        Log.d("debug", String.format("onCheckedChanged: %s, %s", rg, arg1));
    }

    @InjectOnKeyListener(R.id.editText)
    private boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d("debug", String.format("onKey: %s, %s, %s", v, keyCode, event));
        return false;
    }

    private final class CustomServiceResolver implements InjectionResolver {

        @Override
        public Object resolve(Class<?> serviceClass) {
            Object service = null;
            if (SimpleService.class.isAssignableFrom(serviceClass)) {
                service = simpleService;
            }
            return service;
        }
    }

    private static class SimpleService {

    }
}