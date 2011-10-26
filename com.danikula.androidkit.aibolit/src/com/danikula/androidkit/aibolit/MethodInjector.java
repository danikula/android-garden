package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.view.View;

public interface MethodInjector<T extends Annotation> {
    
    void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, T annotation);
    
}
