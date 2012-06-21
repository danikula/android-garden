package com.danikula.android.garden.task;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class TaskServiceHelper {

    private static final long LIFE_TIME_SERVICE_MILLIS = TimeUnit.SECONDS.toMillis(60);

    private ArrayList<TaskResultListener> taskResultListeners = Lists.newArrayList();

    private Set<Integer> pendingRequests = Sets.newHashSet();

    private Handler handler = new Handler();

    private Runnable stopServiceRunnable = new StopServiceRunnable();

    private Application application;

    private Class<? extends Service> serviceClass;

    public TaskServiceHelper(Application app, Class<? extends Service> serviceClass) {
        this.serviceClass = checkNotNull(serviceClass, "Service's class must be not null!");
        this.application = checkNotNull(app, "Application must be not null!");
    }

    public void addListener(TaskResultListener listener) {
        checkNotNull(listener, "Listener must be not null!");
        taskResultListeners.add(listener);
    }

    public void removeListener(TaskResultListener listener) {
        checkNotNull(listener, "Listener must be not null!");
        taskResultListeners.remove(listener);
    }

    public boolean isPending(int requestId) {
        return pendingRequests.contains(requestId);
    }

    public void submitTask(int taskId, String action) {
        submitTask(taskId, action, new Bundle());
    }

    public void submitTask(int taskId, String action, Bundle args) {
        checkNotNull(action, "Action must be not null!");
        checkNotNull(args, "Task's arguments must be not null!");

        if (!isPending(taskId)) {
            Intent intent = newTaskServiceIntent();
            intent.setAction(action);
            intent.putExtra(TaskService.EXTRA_RESULT_RECEIVER, new TaskResultReceiver(taskId));
            intent.putExtras(args);
            application.startService(intent);

            pendingRequests.add(taskId);
        }
    }

    private Intent newTaskServiceIntent() {
        return new Intent(application, serviceClass);
    }

    private void postStopService() {
        handler.removeCallbacks(stopServiceRunnable);
        handler.postDelayed(stopServiceRunnable, LIFE_TIME_SERVICE_MILLIS);
    }

    private void handleTaskResult(int requestId, int resultCode, Bundle resultData) {
        pendingRequests.remove(requestId);
        for (TaskResultListener listener : taskResultListeners) {
            boolean success = resultCode == TaskHandler.RESPONSE_SUCCESS;
            notifyListener(listener, success, requestId, resultData);
        }

        if (pendingRequests.isEmpty()) {
            postStopService();
        }
    }

    private void notifyListener(TaskResultListener listener, boolean success, int requestId, Bundle resultData) {
        if (success) {
            listener.onTaskSuccess(requestId, resultData);
        }
        else {
            Exception error = (Exception) resultData.getSerializable(TaskHandler.RESULT_ARG_ERROR);
            listener.onTaskError(requestId, resultData, error);
        }
    }

    private final class TaskResultReceiver extends ResultReceiver {

        private final int requestId;

        private TaskResultReceiver(int requestId) {
            super(handler);
            this.requestId = requestId;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            handleTaskResult(requestId, resultCode, resultData);
        }
    }

    private final class StopServiceRunnable implements Runnable {

        @Override
        public void run() {
            if (pendingRequests.isEmpty()) {
                application.stopService(newTaskServiceIntent());
            }
        }

    }

}
