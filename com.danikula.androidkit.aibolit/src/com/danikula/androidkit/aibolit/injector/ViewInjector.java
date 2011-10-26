package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Field;

import android.view.View;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.annotation.InjectView;

public class ViewInjector extends AbstractFieldInjector<InjectView> {

    @Override
    public void doInjection(Object fieldOwner, View viewHolder, Field field, InjectView annotation) {
        View view = getViewById(viewHolder, annotation.value());
        checkIsAssignable(field, field.getType(), view.getClass());
        setValue(fieldOwner, field, view);
    }
}
