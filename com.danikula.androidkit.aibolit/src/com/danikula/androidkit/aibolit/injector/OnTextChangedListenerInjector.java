package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.annotation.InjectOnTextChangedListener;

/*package private*/class OnTextChangedListenerInjector extends AbstractMethodInjector<InjectOnTextChangedListener> {

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnTextChangedListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { CharSequence.class, int.class, int.class, int.class };
        Method targetMethod = getMethod(TextWatcher.class, "onTextChanged", argsTypes, sourceMethod);
        TextWatcher textWatcher = createInvokationProxy(TextWatcher.class, methodOwner, sourceMethod, targetMethod);

        View view = getViewById(viewHolder, annotation.value());
        checkIsViewAssignable(TextView.class, view.getClass());
        ((TextView) view).addTextChangedListener(textWatcher);
    }
}
