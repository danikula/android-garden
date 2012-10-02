package com.danikula.android.garden.task;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class TaskServiceHelper {

    private static final long LIFE_TIME_SERVICE_MILLIS = TimeUnit.SECONDS.toMillis(5 * 60);

    private ArrayList<OnTaskResultListener> taskResultListeners = Lists.newArrayList();

    private List<TaskInfo> pendingTasks = Lists.newArrayList();

    private Handler handler = new Handler();

    private AtomicInteger idGenerator = new AtomicInteger();

    private Runnable stopServiceRunnable = new StopServiceRunnable();

    private Map<Integer, Command> commands = new HashMap<Integer, Command>();

    private Application application;

    public TaskServiceHelper(Application app) {
        this.application = checkNotNull(app, "Application must be not null!");
    }

    public <R> void registerCommand(int action, Command command) {
        checkNotNull(action, "Action must be not null!");
        checkNotNull(command, "Command must be not null!");

        commands.put(action, command);
    }

    public void addOnTaskResultListener(OnTaskResultListener listener) {
        checkNotNull(listener, "Listener must be not null!");

        taskResultListeners.add(listener);
    }

    public void removeOnTaskResultListener(OnTaskResultListener listener) {
        checkNotNull(listener, "Listener must be not null!");

        taskResultListeners.remove(listener);
    }

    public boolean isPending(int taskId) {
        return findTask(taskId).isPresent();
    }

    public boolean isAnyTaskWithActionPending(int action) {
        return !findTasksWithAction(action).isEmpty();
    }

    public int submitTask(int action) {
        return submitTask(action, new Bundle());
    }

    public int submitTask(int action, LaunchMode launchMode) {
        return submitTask(action, new Bundle(), launchMode);
    }

    public int submitTask(int action, Bundle args) {
        return submitTask(action, args, LaunchMode.SKIP_IF_RUNNING);
    }

    public int submitTask(int action, Bundle args, LaunchMode launchMode) {
        checkNotNull(args, "Task's arguments must be not null!");
        checkNotNull(launchMode, "Launch mode must be not null!");

        if (launchMode == LaunchMode.SKIP_IF_RUNNING || launchMode == LaunchMode.CANCEL_PREVIOUS) {
            List<TaskInfo> pendingActionTasks = findTasksWithAction(action);
            if (!pendingActionTasks.isEmpty() && launchMode == LaunchMode.SKIP_IF_RUNNING) {
                TaskInfo lastPendingTask = pendingActionTasks.get(pendingActionTasks.size() - 1);
                return lastPendingTask.taskId;
            }
            for (TaskInfo taskInfo : pendingActionTasks) {
                cancel(taskInfo);
            }
        }

        int newTaskId = idGenerator.incrementAndGet();
        TaskInfo taskInfo = new TaskInfo(newTaskId, action);

        application.startService(newStartServiceIntent(args, taskInfo));

        pendingTasks.add(taskInfo);
        return newTaskId;
    }

    public void cancel(int taskId) {
        Optional<TaskInfo> optionalTaskInfo = findTask(taskId);
        if (optionalTaskInfo.isPresent()) {
            cancel(optionalTaskInfo.get());
        }
    }

    private void cancel(TaskInfo taskInfo) {
        // do not really cancel task, just don't deliver result to listeners
        handleTaskResult(taskInfo, ResultStatus.CANCEL, new Bundle());
    }

    private Optional<TaskInfo> findTask(int taskId) {
        return Iterables.tryFind(pendingTasks, new ConcreteTaskPredicate(taskId));
    }

    private Intent newStartServiceIntent(Bundle args, TaskInfo taskInfo) {
        Intent intent = newTaskServiceIntent();
        intent.putExtra(TaskService.EXTRA_COMMAND, getCommand(taskInfo.action));
        intent.putExtra(TaskService.EXTRA_RESULT_RECEIVER, new TaskResultReceiver(taskInfo));
        intent.putExtras(args);
        return intent;
    }

    private List<TaskInfo> findTasksWithAction(int action) {
        Predicate<TaskInfo> predicate = new ActionTaskPredicate(action);
        return new ArrayList<TaskInfo>(Collections2.filter(pendingTasks, predicate));
    }

    private Command getCommand(int action) {
        checkArgument(commands.containsKey(action), "There is no registered command for action '%s'", action);

        return commands.get(action);
    }

    private Intent newTaskServiceIntent() {
        return new Intent(application, TaskService.class);
    }

    private void postStopService() {
        handler.removeCallbacks(stopServiceRunnable);
        handler.postDelayed(stopServiceRunnable, LIFE_TIME_SERVICE_MILLIS);
    }

    private void handleTaskResult(TaskInfo taskInfo, ResultStatus result, Bundle resultData) {
        if (pendingTasks.contains(taskInfo)) {
            pendingTasks.remove(taskInfo);
            for (OnTaskResultListener listener : taskResultListeners) {
                notifyListener(listener, taskInfo, result, resultData);
            }

            if (pendingTasks.isEmpty()) {
                postStopService();
            }
        }
    }

    private void notifyListener(OnTaskResultListener listener, TaskInfo taskInfo, ResultStatus resultStatus, Bundle data) {
        Command command = getCommand(taskInfo.action);
        if (resultStatus == ResultStatus.SUCCESS) {
            Object result = command.unpackResult(data);
            listener.onTaskSuccess(taskInfo.taskId, taskInfo.action, result);
        }
        else if (resultStatus == ResultStatus.FAIL) {
            Exception error = (Exception) data.getSerializable(Command.RESULT_ARG_ERROR);
            listener.onTaskError(taskInfo.taskId, taskInfo.action, error);
        }
        else if (resultStatus == ResultStatus.CANCEL) {
            listener.onTaskCancel(taskInfo.taskId, taskInfo.action);
        }
    }

    private final class ConcreteTaskPredicate implements Predicate<TaskInfo> {

        private int taskId;

        private ConcreteTaskPredicate(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public boolean apply(TaskInfo taskInfo) {
            return taskInfo.taskId == taskId;
        }
    }

    private final class ActionTaskPredicate implements Predicate<TaskInfo> {

        private int action;

        private ActionTaskPredicate(int action) {
            this.action = action;
        }

        @Override
        public boolean apply(TaskInfo taskInfo) {
            return taskInfo.action == action;
        }
    }

    private final class TaskResultReceiver extends ResultReceiver {

        private TaskInfo taskInfo;

        public TaskResultReceiver(TaskInfo taskInfo) {
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
            if (pendingTasks.isEmpty()) {
                application.stopService(newTaskServiceIntent());
            }
        }

    }

}
