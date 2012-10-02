package com.danikula.android.garden.task;

public interface OnTaskResultListener {

    void onTaskSuccess(int taskId, int action, Object result);

    void onTaskError(int taskId, int action, Exception error);
    
    void onTaskCancel(int taskId, int action);

}