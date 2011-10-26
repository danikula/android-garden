package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Field;

import android.view.View;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.annotation.InjectView;

public class InjectorVew extends AbstractFieldInjector<InjectView> {

    @Override
    public void doInjection(Object fieldOwner, View viewHolder, Field field, InjectView annotation) {
        View view = getViewById(viewHolder, annotation.value());

        Class<?> fieldClass = field.getType();
        Class<?> viewClass = view.getClass();
        if (!fieldClass.isAssignableFrom(viewClass)) {
            String errorPatterm = "Can't cast view with type %s to variable '%s' with type %s";
            throw new InjectingException(String.format(errorPatterm, viewClass, field.getName(), fieldClass.getName()));
        }

        setValue(fieldOwner, field, view);
    }
}
