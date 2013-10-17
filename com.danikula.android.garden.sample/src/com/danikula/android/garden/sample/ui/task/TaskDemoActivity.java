package com.danikula.android.garden.sample.ui.task;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.sample.GardenDemoApplication;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.task.AsynkRequestExecutor;
import com.danikula.android.garden.task.OnRequestListener;
import com.danikula.android.garden.task.Request;
import com.danikula.android.garden.task.RequestMode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TaskDemoActivity extends Activity implements OnRequestListener {

    private static final String LOG_TAG = TaskDemoActivity.class.getSimpleName();
    
    @ViewById(R.id.editText1)
    private EditText text1;

    @ViewById(R.id.editText2)
    private EditText text2;

    @ViewById(R.id.modeRadioGroup)
    private RadioGroup modeRadioGroup;

    private AsynkRequestExecutor invoker;

    private int taskId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Aibolit.setInjectedContentView(this, R.layout.task);
        invoker = ((GardenDemoApplication) getApplication()).getInvoker();
    }

    @OnClick(R.id.runRunTest)
    private void onStartTaskButtonClick(View view) {
        int modeButtonId = modeRadioGroup.getCheckedRadioButtonId();
        String modeName = findViewById(modeButtonId).getTag().toString();
        RequestMode mode = RequestMode.valueOf(modeName);
        Request request = new DemoRequest(text1.getText().toString(), text2.getText().toString());
        taskId = invoker.submit(request, mode);
    }
    
    @OnClick(R.id.cancelTest)
    private void onCancelTaskButtonClick(View view) {
        invoker.cancel(taskId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        invoker.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        invoker.removeListener(this);
    }

    @Override
    public void onRequestSuccess(Request request, int requestId, Object result) {
        Toast.makeText(this, result + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestError(Request request, int requestId, Exception error) {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestCancel(Request request, int requestId) {
    }

}