package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import android.view.View;

public abstract class AbstractMethodInjector<T extends Annotation> implements MethodInjector<T> {

    protected void checkMethodSignature(Method sourceMethod, Method targetMethod) {
        Class<?>[] sourceMethodArgTypes = sourceMethod.getParameterTypes();
        Class<?>[] targetMethodArgTypes = targetMethod.getParameterTypes();

        if (!Arrays.equals(sourceMethodArgTypes, targetMethodArgTypes)) {
            throw new InjectingException(String.format("Method has incorrect parameters: %s. Expected: %s",
                    Arrays.toString(targetMethodArgTypes), Arrays.toString(sourceMethodArgTypes)));
        }
        if (!sourceMethod.getReturnType().equals(targetMethod.getReturnType())) {
            throw new InjectingException(String.format("Method has incorrect return type: %s. Expected: %s",
                    targetMethod.getReturnType(), sourceMethod.getReturnType()));
        }
    }

    protected void checkViewClass(Class<? extends View> expectedClass, Class<? extends View> viewClass) {
        if (!expectedClass.isAssignableFrom(viewClass)) {
            throw new InjectingException(String.format("%s can inject method only for %s, but not for %s", getClass().getName(),
                    expectedClass.getName(), viewClass.getName()));
        }
    }

    protected Method getMethod(Class<?> methodOwner, String methodName, Class<?>[] argsTypes) {
        try {
            return methodOwner.getMethod(methodName, argsTypes);
        }
        catch (SecurityException e) {
            throw new IllegalArgumentException(String.format("Error getting method '%s' from class '%s'", methodName,
                    methodOwner.getName()), e);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Error getting method '%s' from class '%s'", methodName,
                    methodOwner.getName()), e);
        }
    }

    protected <H> H createInvokationProxy(Class<H> proxyClass, Object methodOwner, Method sourceMethod, Method targetMethod) {
        MethodInvocationHandler methodInvocationHandler = new MethodInvocationHandler(methodOwner, sourceMethod, targetMethod);
        ClassLoader classLoader = proxyClass.getClassLoader();
        return (H) Proxy.newProxyInstance(classLoader, new Class[] { proxyClass }, methodInvocationHandler);
    }

    protected View getViewById(View viewHolder, int viewId) {
        View view = viewHolder.findViewById(viewId);
        if (view == null) {
            throw new InjectingException(String.format("There is no view with id %s in view %s", viewId, viewHolder));
        }
        return view;
    }

}
