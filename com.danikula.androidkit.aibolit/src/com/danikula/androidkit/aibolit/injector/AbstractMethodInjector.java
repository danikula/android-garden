package com.danikula.androidkit.aibolit.injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import android.view.View;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.MethodInvocationHandler;

public abstract class AbstractMethodInjector<T extends Annotation> extends AbstractInjector<T> {

    public abstract void doInjection(Object methodOwner, View viewHolder, Method sourceMethod, T annotation);

    protected void checkIsViewAssignable(Class<? extends View> expectedClass, Class<? extends View> actualClass) {
        if (!expectedClass.isAssignableFrom(actualClass)) {
            String errorPattern = "Injecting is allowable only for view with type %s, but not for %s";
            throw new InjectingException(String.format(errorPattern, expectedClass.getName(), actualClass.getName()));
        }
    }

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

    protected Method getMethod(Class<?> methodOwner, String methodName, Class<?>[] argsTypes) {
        String errorPattern = "Error getting method named '%s' from class %s";
        try {
            return methodOwner.getMethod(methodName, argsTypes);
        }
        catch (SecurityException e) {
            throw new IllegalArgumentException(String.format(errorPattern, methodName, methodOwner.getName()), e);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format(errorPattern, methodName, methodOwner.getName()), e);
        }
    }

    protected <H> H createInvokationProxy(Class<H> proxyClass, Object methodOwner, Method sourceMethod, Method targetMethod) {
        MethodInvocationHandler methodInvocationHandler = new MethodInvocationHandler(methodOwner, sourceMethod, targetMethod);
        ClassLoader classLoader = proxyClass.getClassLoader();
        return (H) Proxy.newProxyInstance(classLoader, new Class[] { proxyClass }, methodInvocationHandler);
    }
}
