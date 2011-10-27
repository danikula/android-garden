package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnRadioGroupCheckedChangeListener;

public class OnRadioGroupCheckedChangeInjector extends AbstractMethodInjector<InjectOnRadioGroupCheckedChangeListener> {

    private static final String TARGET_METHOD_NAME = "onCheckedChanged";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod,
            InjectOnRadioGroupCheckedChangeListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { RadioGroup.class, int.class };
        Method targetMethod = getMethod(OnCheckedChangeListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(RadioGroup.class, view.getClass());

        OnCheckedChangeListener onCheckedChangeListener = createInvokationProxy(OnCheckedChangeListener.class, methodOwner,
                sourceMethod, targetMethod);
        ((RadioGroup) view).setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
