package com.danikula.android.garden.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkNotNull;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class AsynkRequestExecutor {

    private static final String LOG_TAG = AsynkRequestExecutor.class.getSimpleName();

    private static final long LIFE_TIME_SERVICE_MILLIS = TimeUnit.SECONDS.toMillis(5 * 60);

    private final List<OnRequestListener> listeners = Collections.synchronizedList(Lists.<OnRequestListener> newArrayList());

    private final List<RequestInfo> submittedTasks = Collections.synchronizedList(Lists.<RequestInfo> newArrayList());

    private final Handler handler = new Handler();

    private final AtomicInteger idGenerator = new AtomicInteger();

    private final Runnable stopServiceRunnable = new StopServiceRunnable();

    private final Application application;

    private final boolean logRequests;

    public AsynkRequestExecutor(Application app, boolean logRequests) {
        this.application = checkNotNull(app, "Application must be not null!");
        this.logRequests = logRequests;
    }

    public void addListener(OnRequestListener listener) {
        listeners.add(checkNotNull(listener, "Listener must be not null!"));
    }

    public void removeListener(OnRequestListener listener) {
        listeners.remove(checkNotNull(listener, "Listener must be not null!"));
    }

    public boolean isRequestWithIdSubmitted(int requestId) {
        return findTask(requestId).isPresent();
    }

    public boolean isAnyRequestSubmitted(Class<? extends Request>... requestClasses) {
        for (Class<? extends Request> requestClass : requestClasses) {
            List<RequestInfo> submittedRequests = findSameRequest(requestClass);
            boolean submitted = !submittedRequests.isEmpty();
            if (submitted) {
                return true;
            }
        }
        return false;
    }

    public int submit(Request request) {
        return submit(request, RequestMode.SKIP_IF_RUNNING);
    }

    public int submit(Request request, RequestMode requestMode) {
        checkNotNull(request, "Request must be not null!");
        checkNotNull(requestMode, "Request mode must be not null!");

        if (requestMode == RequestMode.SKIP_IF_RUNNING || requestMode == RequestMode.CANCEL_PREVIOUS) {
            List<RequestInfo> pendingActionTasks = findSameRequest(request.getClass());
            if (!pendingActionTasks.isEmpty() && requestMode == RequestMode.SKIP_IF_RUNNING) {
                RequestInfo lastPendingTask = pendingActionTasks.get(pendingActionTasks.size() - 1);
                return lastPendingTask.id;
            }
            for (RequestInfo taskInfo : pendingActionTasks) {
                cancel(taskInfo);
            }
        }

        int newTaskId = idGenerator.incrementAndGet();
        RequestInfo taskInfo = new RequestInfo(newTaskId, request);
        RequestExecutorService.start(application, request, new TaskResultReceiver(taskInfo));
        submittedTasks.add(taskInfo);

        if (logRequests) {
            String messageFormat = "submit request. taskId: '%s', request: '%s', mode: '%s'";
            Log.d(LOG_TAG, String.format(messageFormat, newTaskId, request, requestMode));
        }

        return newTaskId;
    }

    public void cancel(int taskId) {
        Optional<RequestInfo> optionalTaskInfo = findTask(taskId);
        if (optionalTaskInfo.isPresent()) {
            cancel(optionalTaskInfo.get());
        }
    }

    private void cancel(RequestInfo taskInfo) {
        // do not really cancel task, just don't deliver result to listeners
        handleTaskResult(taskInfo, ResultStatus.CANCEL, new Bundle());
    }

    private Optional<RequestInfo> findTask(int taskId) {
        return Iterables.tryFind(submittedTasks, new ConcreteTaskPredicate(taskId));
    }

    private List<RequestInfo> findSameRequest(Class<? extends Request> requestClass) {
        Predicate<RequestInfo> predicate = new ActionTaskPredicate(requestClass);
        return new ArrayList<RequestInfo>(Collections2.filter(submittedTasks, predicate));
    }

    private void postStopService() {
        handler.removeCallbacks(stopServiceRunnable);
        handler.postDelayed(stopServiceRunnable, LIFE_TIME_SERVICE_MILLIS);
    }

    private void handleTaskResult(RequestInfo taskInfo, ResultStatus result, Bundle resultData) {
        boolean isNotCanceled = findTask(taskInfo.id).isPresent();
        if (isNotCanceled) {
            submittedTasks.remove(taskInfo);

            List<OnRequestListener> listenersCopy = Lists.newArrayList(listeners);
            for (OnRequestListener listener : listenersCopy) {
                notifyListener(listener, taskInfo, result, resultData);
            }

            if (submittedTasks.isEmpty()) {
                postStopService();
            }
        }
    }

    private void notifyListener(OnRequestListener callback, RequestInfo requestInfo, ResultStatus resultStatus, Bundle result) {
        Request request = requestInfo.request;
        int requestId = requestInfo.id;
        String description = String.format("requestId: '%s', request: '%s'", requestId, request);
        switch (resultStatus) {
            case SUCCESS:
                Object payload = request.unpackPayload(result);
                if (logRequests) {
                    Log.d(LOG_TAG, String.format("onRequestSuccess. %s, payload: '%s'", description, payload));
                }
                callback.onRequestSuccess(request, requestId, payload);
                break;
            case FAIL:
                Exception error = request.unpackError(result);
                if (logRequests) {
                    Log.e(LOG_TAG, "onRequestError. " + description, error);
                }
                callback.onRequestError(request, requestId, error);
                break;
            case CANCEL:
                if (logRequests) {
                    Log.d(LOG_TAG, "onRequestCancel. " + description);
                }
                callback.onRequestCancel(request, requestId);
                break;
            default:
                throw new IllegalArgumentException("Undefined status " + resultStatus);
        }
    }

    private final class ConcreteTaskPredicate implements Predicate<RequestInfo> {

        private int taskId;

        private ConcreteTaskPredicate(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public boolean apply(RequestInfo taskInfo) {
            return taskInfo.id == taskId;
        }
    }

    private final class ActionTaskPredicate implements Predicate<RequestInfo> {

        private final Class<? extends Request> requestClass;

        private ActionTaskPredicate(Class<? extends Request> requestClass) {
            this.requestClass = requestClass;
        }

        @Override
        public boolean apply(RequestInfo taskInfo) {
            return taskInfo.request.isSame(requestClass);
        }
    }

    private final class TaskResultReceiver extends ResultReceiver {

        private RequestInfo taskInfo;

        public TaskResultReceiver(RequestInfo taskInfo) {
            super(handler);
            this.taskInfo = taskInfo;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            handleTaskResult(taskInfo, ResultStatus.values()[resultCode], resultData);
        }
    }

    private final class StopServiceRunnable implements Runnable {

        @Override
        public void run() {
            if (submittedTasks.isEmpty()) {
                RequestExecutorService.stop(application);
            }
        }
    }

    private static final class RequestInfo {

        private final int id;

        private final Request request;

        public RequestInfo(int taskId, Request request) {
            this.id = taskId;
            this.request = request;
        }
    }
}
