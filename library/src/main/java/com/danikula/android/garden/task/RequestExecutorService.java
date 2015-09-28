package com.danikula.android.garden.task;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestExecutorService extends Service {

    private static final String LOG_TAG = "RequestExecutorService";
    private static final String EXTRA_REQUEST = "com.danikula.android.garden.task.ExtraCommand";
    private static final String EXTRA_RESULT_RECEIVER = "com.danikula.android.garden.task.ExtraResultReceiver";

    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private Handler uiThreadHandler;

    static void start(Context context, Request request, ResultReceiver callback) {
        Intent intent = new Intent(context, RequestExecutorService.class);
        intent.putExtra(RequestExecutorService.EXTRA_REQUEST, request);
        intent.putExtra(RequestExecutorService.EXTRA_RESULT_RECEIVER, callback);
        context.startService(intent);
    }

    static void stop(Context context) {
        Intent intent = new Intent(context, RequestExecutorService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.uiThreadHandler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (!intent.hasExtra(EXTRA_REQUEST)) {
            Log.e(LOG_TAG, "There is no command in extras");
            return START_NOT_STICKY;
        }

        if (!intent.hasExtra(EXTRA_RESULT_RECEIVER)) {
            Log.e(LOG_TAG, "There is no result receiver in extras");
            return START_NOT_STICKY;
        }

        Request command = (Request) intent.getSerializableExtra(EXTRA_REQUEST);
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        submitTask(command, receiver);

        return START_NOT_STICKY;
    }

    private void submitTask(final Request command, final ResultReceiver receiver) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                runTask(command, receiver);
            }
        });
    }

    private void runTask(Request command, ResultReceiver receiver) {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            command.execute(getApplicationContext(), receiver, uiThreadHandler);
        } catch (Throwable e) {
            // deliver any uncatched errors to TaskServiceHelper
            command.onError(receiver, new RuntimeException(e));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // not bindable service
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        executor.shutdown();
    }
}
