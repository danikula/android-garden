package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.annotation.InjectArrayAdapter;

/*package private*/class ArrayAdapterInjector extends AbstractFieldInjector<InjectArrayAdapter> {

    @Override
    public void doInjection(Object fieldOwner, View viewHolder, Field field, InjectArrayAdapter annotation) {
        Context context = viewHolder.getContext();
        int layoutId = annotation.layoutId();
        int textArrayResourceId = annotation.textArrayResourceId();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, textArrayResourceId, layoutId);

        if (adapter == null) {
            String errorPattern = "Adapter for text array with id 0x%s and layout with id 0x%s for field named '%s' with type %s is not created";
            throw new InjectingException(String.format(errorPattern, Integer.toHexString(textArrayResourceId),
                    Integer.toHexString(layoutId), field.getName(), field.getType()));
        }

        checkIsFieldAssignable(field, field.getType(), adapter.getClass());
        setValue(fieldOwner, field, adapter);
    }
}
