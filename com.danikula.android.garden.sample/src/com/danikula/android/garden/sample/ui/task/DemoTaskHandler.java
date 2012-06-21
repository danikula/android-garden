package com.danikula.android.garden.sample.ui.task;

import java.util.concurrent.TimeUnit;

import com.danikula.android.garden.sample.GardenDemoApplication;
import com.danikula.android.garden.task.TaskHandler;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

public class DemoTaskHandler extends TaskHandler {

    private static final String TAG = "TestActionHandler";

    public static String ACTION_EXAMPLE_ACTION = GardenDemoApplication.PACKAGE.concat(".ACTION_EXAMPLE_ACTION");

    public static String EXTRA_PARAM_1 = GardenDemoApplication.PACKAGE.concat(".EXTRA_PARAM_1");

    public static String EXTRA_PARAM_2 = GardenDemoApplication.PACKAGE.concat(".EXTRA_PARAM_2");

    @Override
    public void execute(Context context, Bundle args, ResultReceiver callback) {
        String arg1 = args.getString(EXTRA_PARAM_1);
        String arg2 = args.getString(EXTRA_PARAM_2);
        Bundle data = new Bundle();

        doWork();

        if (TextUtils.isEmpty(arg1) || TextUtils.isEmpty(arg2)) {
            data.putString("error", "Surprise mothafucka!");
            onError(callback, data, new IllegalArgumentException("Empty parameter!"));
        }
        else {
            data.putString("data", arg1 + arg2);
            onSuccess(callback, data);
        }
    }

    private void doWork() {
        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException e) {
            Log.wtf(TAG, "WTF");
        }
    }

}
