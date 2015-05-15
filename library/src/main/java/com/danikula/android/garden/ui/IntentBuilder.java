package com.danikula.android.garden.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntentBuilder {

    private Intent intent;
    private Context context;

    public IntentBuilder(Context context, Class<? extends Activity> activityClass) {
        checkNotNull(context, "Context must be not null!");
        checkNotNull(activityClass, "Activity class must be not null!");

        intent = new Intent(context, activityClass);
        this.context = context;
    }

    public IntentBuilder activity(Class<? extends Activity> activityClass) {
        intent.setComponent(new ComponentName(context, activityClass.getName()));
        return this;
    }

    public IntentBuilder action(String action) {
        intent.setAction(action);
        return this;
    }

    public IntentBuilder arg(String argName, int argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder arg(String argName, Bundle argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder arg(String argName, String argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder arg(String argName, Serializable argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder arg(String argName, Parcelable argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder arg(String argName, ArrayList<Integer> argValue) {
        intent.putExtra(argName, argValue);
        return this;
    }

    public IntentBuilder clearTop() {
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return this;
    }

    public IntentBuilder newTask() {
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
        return this;
    }

    public Intent build() {
        return intent;
    }

    public void start() {
        context.startActivity(intent);
    }

    public void startClearTop() {
        clearTop();
        context.startActivity(intent);
    }

    public void startForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public void startForResult(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }

}
