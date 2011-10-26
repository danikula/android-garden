package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.danikula.androidkit.aibolit.AbstractMethodInjector;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;

public class InjectorOnTouchListener extends AbstractMethodInjector<InjectOnTouchListener> {

    private static final String INJECTED_METHOD_NAME = "onTouch";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnTouchListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, MotionEvent.class };
        Method targetMethod = getMethod(OnTouchListener.class, INJECTED_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        
        OnTouchListener onTouchListener = createInvokationProxy(OnTouchListener.class, methodOwner, sourceMethod, targetMethod);
        view.setOnTouchListener(onTouchListener);
    }
}
