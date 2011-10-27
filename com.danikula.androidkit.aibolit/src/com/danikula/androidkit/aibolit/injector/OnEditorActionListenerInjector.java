package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnEditorActionListener;

public class OnEditorActionListenerInjector extends AbstractMethodInjector<InjectOnEditorActionListener> {

    private static final String TARGET_METHOD_NAME = "onEditorAction";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnEditorActionListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { TextView.class, int.class, KeyEvent.class };
        Method targetMethod = getMethod(OnEditorActionListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(TextView.class, view.getClass());

        OnEditorActionListener onEditorActionListener = createInvokationProxy(OnEditorActionListener.class, methodOwner,
                sourceMethod, targetMethod);
        ((TextView)view).setOnEditorActionListener(onEditorActionListener);
    }
}
