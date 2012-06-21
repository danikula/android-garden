package com.danikula.android.garden.sample.ui.task;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.sample.GardenDemoApplication;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.task.TaskResultListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TaskDemoActivity extends Activity implements TaskResultListener {

    @ViewById(R.id.editText1)
    private EditText text1;

    @ViewById(R.id.editText2)
    private EditText text2;
    
    private DemoTaskServiceHelper taskServiceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.task);
        taskServiceHelper = ((GardenDemoApplication) getApplication()).getServiceHelper();
    }

    @OnClick(R.id.button_button)
    private void onStartTaskButtonClick(View view) {
        taskServiceHelper.executeTestTask(text1.getText().toString(), text2.getText().toString());
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        taskServiceHelper.addListener(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        taskServiceHelper.removeListener(this);
    }

    @Override
    public void onTaskSuccess(int requestId, Bundle result) {
        Toast.makeText(this, result.getString("data"), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskError(int requestId, Bundle result, Exception error) {
        Toast.makeText(this, result.getString("error"), Toast.LENGTH_LONG).show();
    }

}