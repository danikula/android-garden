package com.danikula.android.garden.sample.ui.task;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.sample.GardenDemoApplication;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.task.OnTaskResultListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TaskDemoActivity extends Activity implements OnTaskResultListener {

    private static final String LOG_TAG = TaskDemoActivity.class.getSimpleName();
    
    @ViewById(R.id.editText1)
    private EditText text1;

    @ViewById(R.id.editText2)
    private EditText text2;

    private DemoTaskServiceHelper taskServiceHelper;

    private int taskId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.task);
        taskServiceHelper = ((GardenDemoApplication) getApplication()).getServiceHelper();
    }

    @OnClick(R.id.runRunTest)
    private void onStartTaskButtonClick(View view) {
        taskId = taskServiceHelper.executeTestTask(text1.getText().toString(), text2.getText().toString());
    }
    
    @OnClick(R.id.cancelTest)
    private void onCancelTaskButtonClick(View view) {
        taskServiceHelper.cancel(taskId);
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
    public void onTaskSuccess(int taskId, int action, Object result) {
        Toast.makeText(this, result + "", Toast.LENGTH_LONG).show();
        
        Log.d(LOG_TAG, String.format("onTaskSuccess. taskId: %s, result: %s", taskId, result));
    }

    @Override
    public void onTaskError(int taskId, int action, Object result, Exception error) {
        Toast.makeText(this, result + "", Toast.LENGTH_LONG).show();
        
        Log.d(LOG_TAG, String.format("onTaskError. taskId: %s, result: %s", taskId, result));
    }

    @Override
    public void onTaskCancel(int taskId, int action) {
        Log.d(LOG_TAG, String.format("onTaskCancel. taskId: %s", taskId));
    }

}