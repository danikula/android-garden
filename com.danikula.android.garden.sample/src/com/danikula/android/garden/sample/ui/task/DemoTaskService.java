package com.danikula.android.garden.sample.ui.task;

import com.danikula.android.garden.task.TaskService;

public class DemoTaskService extends TaskService {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        registerTaskHandler(DemoTaskHandler.ACTION_EXAMPLE_ACTION, new DemoTaskHandler());
    }

}
