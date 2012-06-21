package com.danikula.android.garden.sample;

import com.danikula.android.garden.sample.ui.task.DemoTaskServiceHelper;

import android.app.Application;

public class GardenDemoApplication extends Application {
    
    public static final String PACKAGE = "com.danikula.android.garden.sample";
    
    private DemoTaskServiceHelper serviceHelper;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        serviceHelper = new DemoTaskServiceHelper(this);
    }

    public DemoTaskServiceHelper getServiceHelper() {
        return serviceHelper;
    }

}
