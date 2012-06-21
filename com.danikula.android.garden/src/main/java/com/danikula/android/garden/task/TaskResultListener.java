package com.danikula.android.garden.task;

import android.os.Bundle;

public interface TaskResultListener {

    void onTaskSuccess(int requestId, Bundle result);

    void onTaskError(int requestId, Bundle result, Exception error);

}