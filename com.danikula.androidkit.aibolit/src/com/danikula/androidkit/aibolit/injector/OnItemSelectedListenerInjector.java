package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnItemSelectedListener;

/*package private*/class OnItemSelectedListenerInjector extends AbstractMethodInjector<InjectOnItemSelectedListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnItemSelectedListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { AdapterView.class, View.class, int.class, long.class };
        Method targetMethod = getMethod(OnItemSelectedListener.class, "onItemSelected", argsTypes, sourceMethod);
        OnItemSelectedListener onItemSelectedListener = createInvokationProxy(OnItemSelectedListener.class, methodOwner,
                sourceMethod, targetMethod);

        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(AdapterView.class, view.getClass());
        ((AdapterView<?>) view).setOnItemSelectedListener(onItemSelectedListener);
    }
}
