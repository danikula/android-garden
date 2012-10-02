package com.danikula.android.garden.task;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public abstract class Command implements Serializable {

    public static final String RESULT_ARG_ERROR = "com.danikula.android.garden.ArgumentError";

    public abstract void execute(Context context, Bundle args, ResultReceiver callback, Handler uiThreadHandler);

    public Object unpackResult(Bundle packedResult) {
        return null;
    }

    protected void onSuccess(ResultReceiver callback, Bundle result) {
        callback.send(ResultStatus.SUCCESS.ordinal(), result);
    }

    protected void onSuccess(ResultReceiver callback) {
        onSuccess(callback, new Bundle());
    }

    protected void onError(ResultReceiver callback, Bundle result, Exception error) {
        result.putSerializable(RESULT_ARG_ERROR, error);
        callback.send(ResultStatus.FAIL.ordinal(), result);
    }

    public void onError(ResultReceiver callback, Exception error) {
        onError(callback, new Bundle(), error);
    }
}
