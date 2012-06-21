package com.danikula.android.garden.sample.ui.task;

import com.danikula.android.garden.task.TaskResultListener;
import com.danikula.android.garden.task.TaskServiceHelper;

import android.app.Application;
import android.app.Service;
import android.os.Bundle;

public class DemoTaskServiceHelper {

    public static final int TEST_TASK_ID = 12;

    private TaskServiceHelper taskServiceHelper;

    public DemoTaskServiceHelper(Application app, Class<? extends Service> serviceClass) {
        this.taskServiceHelper = new TaskServiceHelper(app, serviceClass);
    }

    public void executeTestTask(String argumentA, String argumentB) {
        Bundle args = new Bundle();
        args.putString(DemoTaskHandler.EXTRA_PARAM_1, argumentA);
        args.putString(DemoTaskHandler.EXTRA_PARAM_2, argumentB);
        taskServiceHelper.submitTask(TEST_TASK_ID, DemoTaskHandler.ACTION_EXAMPLE_ACTION, args);
    }

    // --- simple delegating --- //
    public void addListener(TaskResultListener currentListener) {
        taskServiceHelper.addListener(currentListener);
    }

    public void removeListener(TaskResultListener currentListener) {
        taskServiceHelper.removeListener(currentListener);
    }

    public boolean isPending(int requestId) {
        return taskServiceHelper.isPending(requestId);
    }
}
