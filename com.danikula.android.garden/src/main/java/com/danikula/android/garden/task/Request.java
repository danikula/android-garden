package com.danikula.android.garden.task;

import java.io.Serializable;

import com.danikula.android.garden.utils.Bundles;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public abstract class Request implements Serializable {

    static final String RESULT_ARG_ERROR = "com.danikula.android.garden.ArgumentError";
    static final String RESULT_ARG_PAYLOAD = "com.danikula.android.garden.ArgumentPayload";

    private final boolean deliverResult;

    public Request() {
        this(false);
    }

    public Request(boolean deliverResult) {
        this.deliverResult = deliverResult;
    }

    public abstract void execute(Context context, ResultReceiver callback, Handler uiThreadHandler);

    static Object unpackPayload(Bundle result) {
        return result != null && result.containsKey(RESULT_ARG_PAYLOAD) ? result.getSerializable(RESULT_ARG_PAYLOAD) : null;
    }

    static Exception unpackError(Bundle result) {
        Bundles.checkContains(result, RESULT_ARG_ERROR);
        return (Exception) result.getSerializable(RESULT_ARG_ERROR);
    }

    protected void onSuccess(ResultReceiver callback, Serializable result) {
        if (deliverResult && result != null) {
            Bundle packedResult = new Bundle();
            packedResult.putSerializable(RESULT_ARG_PAYLOAD, result);
            callback.send(ResultStatus.SUCCESS.ordinal(), packedResult);
        } else {
            callback.send(ResultStatus.SUCCESS.ordinal(), null);
        }
    }

    protected void onSuccess(ResultReceiver callback) {
        onSuccess(callback, null);
    }

    protected void onError(ResultReceiver callback, Exception error) {
        Bundle result = new Bundle();
        result.putSerializable(RESULT_ARG_ERROR, error);
        callback.send(ResultStatus.FAIL.ordinal(), result);
    }

    public boolean isSame(Class<? extends Request>... requestClasses) {
        for (Class<? extends Request> requestClass : requestClasses) {
            if (getClass().equals(requestClass)) {
                return true;
            }
        }
        return false;
    }
}
