package com.danikula.android.garden.task;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

public abstract class TaskHandler {

    public static final String RESULT_ARG_ERROR = "com.danikula.android.garden.ArgumentError";

    public static final int RESPONSE_SUCCESS = 0;

    public static final int RESPONSE_FAILURE = 1;

    public abstract void execute(Context context, Bundle args, ResultReceiver callback);

    protected void onSuccess(ResultReceiver callback, Bundle result) {
        callback.send(RESPONSE_SUCCESS, result);
    }

    protected void onSuccess(ResultReceiver callback) {
        onSuccess(callback, new Bundle());
    }

    protected void onError(ResultReceiver callback, Bundle result, Exception error) {
        result.putSerializable(RESULT_ARG_ERROR, error);
        callback.send(RESPONSE_FAILURE, result);
    }

    public void onError(ResultReceiver callback, Exception error) {
        onError(callback, new Bundle(), error);
    }
}
