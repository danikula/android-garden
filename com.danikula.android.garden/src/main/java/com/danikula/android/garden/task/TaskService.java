package com.danikula.android.garden.task;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

public class TaskService extends Service {

    public static final String LOG_TAG = TaskService.class.getName();

    public static final String EXTRA_RESULT_RECEIVER = "ExtraResultReceiver";

    private ExecutorService executor = Executors.newFixedThreadPool(3);

    private Map<String, TaskHandler> taskHandlers = new HashMap<String, TaskHandler>();

    protected void registerTaskHandler(String action, TaskHandler taskHandler) {
        checkNotNull(action, "Action must be not null!");
        checkNotNull(taskHandler, "Task handler must be not null!");

        taskHandlers.put(action, taskHandler);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "TaskService.onStartCommand");

        String action = intent.getAction();
        checkArgument(intent.hasExtra(EXTRA_RESULT_RECEIVER), "There is no result receiver in extras");
        checkArgument(taskHandlers.containsKey(action), "There is no registered task handler for action '%s'", action);

        TaskHandler taskHandler = taskHandlers.get(action);
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        submitTask(taskHandler, intent.getExtras(), receiver);

        return START_STICKY;
    }

    private void submitTask(final TaskHandler taskHandler, final Bundle args, final ResultReceiver receiver) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    taskHandler.execute(getApplicationContext(), args, receiver);
                }
                catch (Exception e) {
                    // deliver any exception to TaskServiceHelper
                    taskHandler.onError(receiver, e);
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // not bindable service
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "TaskService.onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "TaskService.onDestroy");
    }
}
