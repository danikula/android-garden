package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnFocusChangeListener;

public class OnFocusChangeListenerInjector extends AbstractMethodInjector<InjectOnFocusChangeListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnFocusChangeListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, boolean.class };
        Method targetMethod = getMethod(OnFocusChangeListener.class, "onFocusChange", argsTypes, sourceMethod);
        OnFocusChangeListener onFocusChangeListener = createInvokationProxy(OnFocusChangeListener.class, methodOwner,
                sourceMethod, targetMethod);

        View view = getViewById(viewHolder, annotation.value());
        view.setOnFocusChangeListener(onFocusChangeListener);
    }
}
