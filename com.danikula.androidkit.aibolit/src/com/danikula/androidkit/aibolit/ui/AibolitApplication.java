package com.danikula.androidkit.aibolit.ui;

import android.app.Application;

import com.danikula.androidkit.aibolit.Aibolit;
import com.danikula.androidkit.aibolit.ServicesResolver;

public class AibolitApplication extends Application implements ServicesResolver{
    
    private HttpManager httpManager;

    @Override
    public void onCreate() {
        super.onCreate();
        
        Aibolit.addInjectionResolver(this);
    }
    
    @Override
    public Object resolve(Class<?> serviceClass) {
        Object service = null;
        if (HttpManager.class.isAssignableFrom(serviceClass)) {
            service = getHttpManager();
        }
        // else if (...) {...} resolve all custom services
        return service;
    }
    
    private HttpManager getHttpManager() {
        if (httpManager == null) {
            httpManager = new HttpManager();
        }
        return httpManager;
    }
    
    // some application service. should be top level class
    public static class HttpManager {
        
        public Object invokeRequest(Object request) {
            Object response = null; 
            // do work... 
            return response;
        }
    }
    
}
