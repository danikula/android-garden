package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnItemClickListener;

public class OnItemClickListenerInjector extends AbstractMethodInjector<InjectOnItemClickListener> {

    private static final String TARGET_METHOD_NAME = "onItemClick";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnItemClickListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { AdapterView.class, View.class, int.class, long.class };
        Method targetMethod = getMethod(OnItemClickListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        
        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(AdapterView.class, view.getClass());
        AdapterView<?> adapterView = (AdapterView<?>) view; 

        OnItemClickListener onItemClickListener = createInvokationProxy(OnItemClickListener.class, methodOwner, sourceMethod, targetMethod);
        adapterView.setOnItemClickListener(onItemClickListener);
    }
}
