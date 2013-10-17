package com.danikula.android.garden.sample.ui.task;

import com.danikula.android.garden.task.Request;
import com.danikula.android.garden.utils.StringUtils;
import com.danikula.android.garden.utils.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;

public class DemoRequest extends Request {

    private String login;

    private String password;

    public DemoRequest(String login, String password) {
        super(true);
        this.login = login;
        this.password = password;
    }

    @Override
    public void execute(Context context, ResultReceiver callback, Handler handler) {
        if (StringUtils.isAnyEmpty(login, password)) {
            onError(callback, new IllegalArgumentException("Empty parameter!"));
        } else {
            doWork();
            String result = login + password;
            onSuccess(callback, result);
        }
    }

    private void doWork() {
        Utils.sleepOnMs(2000);
    }
}
