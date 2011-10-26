package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;

import com.danikula.androidkit.aibolit.annotation.InjectOnCreateContextMenuListener;

public class OnCreateContextMenuListenerInjector extends AbstractMethodInjector<InjectOnCreateContextMenuListener> {

    private static final String TARGET_METHOD_NAME = "onCreateContextMenu";

    @Override
    public void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, InjectOnCreateContextMenuListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { ContextMenu.class, View.class, int.class, ContextMenuInfo.class };
        Method targetMethod = getMethod(OnCreateContextMenuListener.class, TARGET_METHOD_NAME, argsTypes);
        checkMethodSignature(targetMethod, sourceMethod);
        View view = getViewById(viewHolder, annotation.value());

        OnCreateContextMenuListener onCreateContextMenuListener = createInvokationProxy(OnCreateContextMenuListener.class,
                methodOwner, sourceMethod, targetMethod);
        view.setOnCreateContextMenuListener(onCreateContextMenuListener);
    }
}
