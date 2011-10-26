package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnCheckedChangeListener;

public class OnCheckedChangeInjector extends AbstractMethodInjector<InjectOnCheckedChangeListener> {

    private static final String TARGET_METHOD_NAME = "onCheckedChanged";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnCheckedChangeListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { CompoundButton.class, boolean.class };
        Method targetMethod = getMethod(OnCheckedChangeListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        checkViewClass(CompoundButton.class, view.getClass());

        OnCheckedChangeListener onCheckedChangeListener = createInvokationProxy(OnCheckedChangeListener.class, methodOwner,
                sourceMethod, targetMethod);
        ((CompoundButton) view).setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
