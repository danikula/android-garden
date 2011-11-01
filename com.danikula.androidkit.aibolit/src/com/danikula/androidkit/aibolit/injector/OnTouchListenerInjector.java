package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.danikula.androidkit.aibolit.InjectionContext;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;

/**
 * Injects {@link View.OnTouchListener#onTouch(View, MotionEvent)} method
 * 
 * @author Alexey Danilov
 * 
 */
/* package private */class OnTouchListenerInjector extends AbstractMethodInjector<InjectOnTouchListener> {

    @Override
    public void doInjection(Object methodOwner, InjectionContext injectionContext, Method sourceMethod, InjectOnTouchListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { View.class, MotionEvent.class };
        Method targetMethod = getMethod(OnTouchListener.class, "onTouch", argsTypes, sourceMethod);
        OnTouchListener onTouchListener = createInvokationProxy(OnTouchListener.class, methodOwner, sourceMethod, targetMethod);

        View view = getViewById(injectionContext.getRootView(), annotation.value());
        view.setOnTouchListener(onTouchListener);
    }
}
