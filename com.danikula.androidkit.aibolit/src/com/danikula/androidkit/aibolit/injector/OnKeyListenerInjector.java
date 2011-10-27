package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnKeyListener;

/*package private*/class OnKeyListenerInjector extends AbstractMethodInjector<InjectOnKeyListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnKeyListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, int.class, KeyEvent.class };
        Method targetMethod = getMethod(OnKeyListener.class, "onKey", argsTypes, sourceMethod);
        OnKeyListener onKeyListener = createInvokationProxy(OnKeyListener.class, methodOwner, sourceMethod, targetMethod);

        View view = getViewById(viewHolder, annotation.value());
        view.setOnKeyListener(onKeyListener);
    }
}
