package com.danikula.android.garden.sample;

import com.danikula.android.garden.task.AsynkRequestExecutor;

import android.app.Application;

public class GardenDemoApplication extends Application {
    
    public static final String PACKAGE = "com.danikula.android.garden.sample";
    
    private AsynkRequestExecutor invoker;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        invoker = new AsynkRequestExecutor(this, true);
    }

    public AsynkRequestExecutor getInvoker() {
        return invoker;
    }

}
