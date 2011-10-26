package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnLongClickListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnLongClickListener;

public class OnLongClickListenerInjector extends AbstractMethodInjector<InjectOnLongClickListener> {

    private static final String INJECTED_METHOD_NAME = "onLongClick";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnLongClickListener annotation) {
        Method targetMethod = getMethod(OnLongClickListener.class, INJECTED_METHOD_NAME, new Class<?>[] { View.class });
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());

        OnLongClickListener listener = createInvokationProxy(OnLongClickListener.class, methodOwner, sourceMethod, targetMethod);
        view.setOnLongClickListener(listener);
    }

}
