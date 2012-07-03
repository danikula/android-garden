package com.danikula.android.garden.sample.ui.task;

import com.danikula.android.garden.task.LaunchMode;
import com.danikula.android.garden.task.OnTaskResultListener;
import com.danikula.android.garden.task.TaskServiceHelper;

import android.app.Application;
import android.os.Bundle;

public class DemoTaskServiceHelper {

    public static final int TEST_TASK_ACTION = 12;

    private TaskServiceHelper taskServiceHelper;

    public DemoTaskServiceHelper(Application app) {
        this.taskServiceHelper = new TaskServiceHelper(app);
        taskServiceHelper.registerCommand(TEST_TASK_ACTION, new DemoCommand());
    }

    public int executeTestTask(String argumentA, String argumentB) {
        Bundle args = new Bundle();
        args.putString(DemoCommand.EXTRA_PARAM_1, argumentA);
        args.putString(DemoCommand.EXTRA_PARAM_2, argumentB);
        return taskServiceHelper.submitTask(TEST_TASK_ACTION, args, LaunchMode.CANCEL_PREVIOUS);
    }

    // --- simple delegating --- //
    public void addListener(OnTaskResultListener currentListener) {
        taskServiceHelper.addOnTaskResultListener(currentListener);
    }

    public void removeListener(OnTaskResultListener currentListener) {
        taskServiceHelper.removeOnTaskResultListener(currentListener);
    }

    public boolean isPending(int requestId) {
        return taskServiceHelper.isPending(requestId);
    }

    public void cancel(int taskId) {
        taskServiceHelper.cancel(taskId);
    }
    
}
