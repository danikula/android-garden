package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnRadioGroupCheckedChangeListener;

/*package private*/class OnRadioGroupCheckedChangeInjector extends AbstractMethodInjector<InjectOnRadioGroupCheckedChangeListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod,
            InjectOnRadioGroupCheckedChangeListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { RadioGroup.class, int.class };
        Method targetMethod = getMethod(OnCheckedChangeListener.class, "onCheckedChanged", argsTypes, sourceMethod);
        OnCheckedChangeListener onCheckedChangeListener = createInvokationProxy(OnCheckedChangeListener.class, methodOwner,
                sourceMethod, targetMethod);
        
        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(RadioGroup.class, view.getClass());
        ((RadioGroup) view).setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
