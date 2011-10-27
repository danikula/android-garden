package com.danikula.androidkit.aibolit.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.Aibolit;
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
import com.danikula.androidkit.aibolit.annotation.InjectResource;
import com.danikula.androidkit.aibolit.annotation.InjectService;
import com.danikula.androidkit.aibolit.annotation.InjectSystemService;
import com.danikula.androidkit.aibolit.annotation.InjectView;
import com.danikula.androidkit.aibolit.ui.AibolitApplication.HttpManager;

public class TestInjectActivity extends Activity {

    @InjectService
    private HttpManager httpManager;

    @InjectView(R.id.textView)
    public TextView textView;

    @InjectView(R.id.listView)
    public ListView listView;

    @InjectSystemService(Context.LAYOUT_INFLATER_SERVICE)
    private LayoutInflater layoutInflater;
    
    @InjectResource(R.anim.my_anim)
    private Animation animation;
    
    @InjectResource(R.color.button_text)
    private ColorStateList buttonText;

    @InjectResource(R.color.translucent_red)
    private int redColor;

    @InjectResource(android.R.drawable.btn_default)
    private Drawable drawable;

    @InjectResource(android.R.layout.simple_expandable_list_item_2)
    private View view;

    @InjectResource(R.array.numbers)
    private String[] numbers;
    
    @InjectResource(R.array.icons)
    private TypedArray icons;
    
    @InjectResource(R.array.integers)
    private int[] integers;
    
    @InjectResource(R.bool.screen_small)
    private boolean screenSmall;

    @InjectResource(R.dimen.font_size)
    private float fontSize;

    @InjectResource(R.integer.max_speed)
    private int maxSpeed;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setContentView(this, R.layout.test);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        Log.d("debug", "LayoutInflater: " + layoutInflater);
        Log.d("debug", "HttpManager: " + httpManager);
        Log.d("debug", "animation: " + animation);
        Log.d("debug", "buttonText: " + buttonText);
        Log.d("debug", "redColor: " + redColor);
        Log.d("debug", "drawable: " + drawable);
        Log.d("debug", "view: " + view);
        Log.d("debug", "numbers: " + numbers);
        Log.d("debug", "icons: " + icons);
        Log.d("debug", "integers: " + integers);
        Log.d("debug", "screenSmall: " + screenSmall);
        Log.d("debug", "fontSize: " + fontSize);
        Log.d("debug", "maxSpeed: " + maxSpeed);
    }

    @InjectOnClickListener(R.id.button)
    private void onButtonClickListener(View view) {
        Log.d("debug", "onButtonClickListener!");
        httpManager.invokeRequest(new Object());
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
}