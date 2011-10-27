package com.danikula.androidkit.aibolit.injector;

import java.lang.annotation.Annotation;

import android.view.View;

import com.danikula.androidkit.aibolit.InjectingException;

public abstract class AbstractInjector<T extends Annotation> {

    protected View getViewById(View viewHolder, int viewId) {
        View view = viewHolder.findViewById(viewId);
        if (view == null) {
            String errorPattern = "There is no view with id 0x%s in view %s";
            throw new InjectingException(String.format(errorPattern, Integer.toHexString(viewId), viewHolder));
        }
        return view;
    }

}
