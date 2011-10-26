package com.danikula.androidkit.aibolit.injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.danikula.androidkit.aibolit.FieldInjector;
import com.danikula.androidkit.aibolit.InjectingException;

import android.view.View;

public abstract class AbstractFieldInjector<A extends Annotation> implements FieldInjector<A> {

    protected View getViewById(View viewHolder, int viewId) {
        View view = viewHolder.findViewById(viewId);
        if (view == null) {
            throw new InjectingException(String.format("There is no view with id %s in view %s", viewId, viewHolder));
        }
        return view;
    }
    
    protected void setValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        }
        catch (IllegalAccessException e) {
            throw new InjectingException(String.format("Error setting value '%s' to field '%s'", value, field, e));
        }
    }

}
