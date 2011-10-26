package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnFocusChangeListener;

public class OnFocusChangeListenerInjector extends AbstractMethodInjector<InjectOnFocusChangeListener> {

    private static final String TARGET_METHOD_NAME = "onFocusChange";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnFocusChangeListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, boolean.class };
        Method targetMethod = getMethod(OnFocusChangeListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());

        OnFocusChangeListener onFocusChangeListener = createInvokationProxy(OnFocusChangeListener.class, methodOwner,
                sourceMethod, targetMethod);
        view.setOnFocusChangeListener(onFocusChangeListener);
    }
}
