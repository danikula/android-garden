package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.annotation.InjectOnTextChangedListener;

public class OnTextChangedListenerInjector extends AbstractMethodInjector<InjectOnTextChangedListener> {

    private static final String TARGET_METHOD_NAME = "onTextChanged";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnTextChangedListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { CharSequence.class, int.class, int.class, int.class };
        Method targetMethod = getMethod(TextWatcher.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());
        checkViewClass(TextView.class, view.getClass());

        TextWatcher textWatcher = createInvokationProxy(TextWatcher.class, methodOwner, sourceMethod, targetMethod);
        ((TextView) view).addTextChangedListener(textWatcher);
    }
}
