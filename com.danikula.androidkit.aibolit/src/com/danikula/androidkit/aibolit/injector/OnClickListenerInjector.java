package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;

/**
 * Injects {@link View.OnClickListener#onClick(View)} method
 * 
 * @author Alexey Danilov
 * 
 */
/*package private*/class OnClickListenerInjector extends AbstractMethodInjector<InjectOnClickListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnClickListener annotation) {
        Method targetMethod = getMethod(OnClickListener.class, "onClick", new Class<?>[] { View.class }, sourceMethod);
        OnClickListener onClickListener = createInvokationProxy(OnClickListener.class, methodOwner, sourceMethod, targetMethod);

        View view = getViewById(viewHolder, annotation.value());
        view.setOnClickListener(onClickListener);
    }

}
