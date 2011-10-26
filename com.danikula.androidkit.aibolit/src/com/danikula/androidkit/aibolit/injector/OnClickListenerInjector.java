package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;

public class OnClickListenerInjector extends AbstractMethodInjector<InjectOnClickListener> {

    private static final String TARGET_METHOD_NAME = "onClick";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnClickListener annotation) {
        Method targetMethod = getMethod(OnClickListener.class, TARGET_METHOD_NAME, new Class<?>[] { View.class });
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        
        OnClickListener onClickListener = createInvokationProxy(OnClickListener.class, methodOwner, sourceMethod, targetMethod);
        view.setOnClickListener(onClickListener);
    }

}
