package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnKeyListener;

public class OnKeyListenerInjector extends AbstractMethodInjector<InjectOnKeyListener> {

    private static final String TARGET_METHOD_NAME = "onKey";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnKeyListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, int.class, KeyEvent.class };
        Method targetMethod = getMethod(OnKeyListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());

        OnKeyListener onKeyListener = createInvokationProxy(OnKeyListener.class, methodOwner, sourceMethod, targetMethod);
        view.setOnKeyListener(onKeyListener);
    }
}
