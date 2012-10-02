package com.danikula.android.garden.sample.ui.task;

import com.danikula.android.garden.sample.GardenDemoApplication;
import com.danikula.android.garden.task.Command;
import com.danikula.android.garden.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;

public class DemoCommand extends Command {

    private static final String RESULT_ERROR = "error";

    private static final String RESULT_ARG = "data";

    private static final String TAG = "TestActionHandler";

    public static String EXTRA_PARAM_1 = GardenDemoApplication.PACKAGE.concat(".EXTRA_PARAM_1");

    public static String EXTRA_PARAM_2 = GardenDemoApplication.PACKAGE.concat(".EXTRA_PARAM_2");

    @Override
    public void execute(Context context, Bundle args, ResultReceiver callback, Handler handler) {
        String arg1 = args.getString(EXTRA_PARAM_1);
        String arg2 = args.getString(EXTRA_PARAM_2);
        Bundle data = new Bundle();

        doWork();

        if (TextUtils.isEmpty(arg1) || TextUtils.isEmpty(arg2)) {
            data.putString(RESULT_ERROR, "Surprise mothafucka!");
            onError(callback, data, new IllegalArgumentException("Empty parameter!"));
        }
        else {
            data.putString(RESULT_ARG, arg1 + arg2);
            onSuccess(callback, data);
        }
    }

    @Override
    public Object unpackResult(Bundle packedResult) {
        return packedResult.getString(RESULT_ARG);
    }
    
    private void doWork() {
        Utils.sleepOnMs(2000);
    }

}
