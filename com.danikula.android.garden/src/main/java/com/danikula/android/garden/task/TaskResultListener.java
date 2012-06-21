package com.danikula.android.garden.task;

public interface TaskResultListener {

    void onTaskSuccess(int taskId, Object result);

    void onTaskError(int taskId, Object errorData, Exception error);
    
    void onTaskCancel(int taskId);

}