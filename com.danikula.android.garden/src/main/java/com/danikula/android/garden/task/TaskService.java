package com.danikula.android.garden.task;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.ResultReceiver;
import android.util.Log;

public class TaskService extends Service {

    public static final String LOG_TAG = TaskService.class.getName();

    public static final String EXTRA_COMMAND = "com.danikula.android.garden.task.ExtraCommand";

    public static final String EXTRA_RESULT_RECEIVER = "com.danikula.android.garden.task.ExtraResultReceiver";

    private ExecutorService executor = Executors.newFixedThreadPool(3);
    
    private Handler uiThreadHandler = new Handler();

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "TaskService.onStartCommand");

        checkArgument(intent.hasExtra(EXTRA_COMMAND), "There is no command in extras");
        checkArgument(intent.hasExtra(EXTRA_RESULT_RECEIVER), "There is no result receiver in extras");

        Command command = (Command) intent.getSerializableExtra(EXTRA_COMMAND);
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        submitTask(command, intent.getExtras(), receiver);

        return START_NOT_STICKY;
    }

    private void submitTask(final Command command, final Bundle args, final ResultReceiver receiver) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                runTask(command, args, receiver);
            }
        });
    }

    private void runTask(Command command, Bundle args, ResultReceiver receiver) {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            
            command.execute(getApplicationContext(), args, receiver, uiThreadHandler);
        }
        catch (Exception e) {
            // deliver any exception to TaskServiceHelper
            command.onError(receiver, e);
        }
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
        
        executor.shutdown();
    }
}
