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

    protected void checkIsAssignable(Field field, Class<?> fieldClass, Class<?> viewClass) {
        if (!fieldClass.isAssignableFrom(viewClass)) {
            String errorPatterm = "Can't cast object with type %s to field named '%s' with type %s";
            throw new InjectingException(String.format(errorPatterm, viewClass, field.getName(), fieldClass.getName()));
        }
    }

    protected void setValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        }
        catch (IllegalArgumentException e) {
            processSettingValueError(field, value, e);
        }
        catch (IllegalAccessException e) {
            processSettingValueError(field, value, e);
        }
    }

    private void processSettingValueError(Field field, Object value, Exception e) throws InjectingException {
        String errorPattern = "Error setting value '%s' with type %s to field named '%s' with type %s";
        String error = String.format(errorPattern, value, value.getClass().getName(), field.getName(), field.getType().getName());
        throw new InjectingException(error, e);
    }

}
