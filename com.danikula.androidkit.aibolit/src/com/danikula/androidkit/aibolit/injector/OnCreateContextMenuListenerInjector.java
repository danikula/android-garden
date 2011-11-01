package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Method;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;

import com.danikula.androidkit.aibolit.InjectionContext;
import com.danikula.androidkit.aibolit.annotation.InjectOnCreateContextMenuListener;

/**
 * Injects {@link OnCreateContextMenuListener#onCreateContextMenu(ContextMenu, View, ContextMenuInfo)} method
 * 
 * @author Alexey Danilov
 * 
 */
/* package private */class OnCreateContextMenuListenerInjector extends AbstractMethodInjector<InjectOnCreateContextMenuListener> {

    @Override
    public void doInjection(Object methodOwner, InjectionContext injectionContext, Method sourceMethod, InjectOnCreateContextMenuListener annotation) {
        Class<?>[] argsTypes = new Class<?>[] { ContextMenu.class, View.class, int.class, ContextMenuInfo.class };
        Method targetMethod = getMethod(OnCreateContextMenuListener.class, "onCreateContextMenu", argsTypes, sourceMethod);
        OnCreateContextMenuListener onCreateContextMenuListener = createInvokationProxy(OnCreateContextMenuListener.class,
                methodOwner, sourceMethod, targetMethod);

        View view = getViewById(injectionContext.getRootView(), annotation.value());
        view.setOnCreateContextMenuListener(onCreateContextMenuListener);
    }
}
