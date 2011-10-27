package com.danikula.androidkit.aibolit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler for injected methods. 
 * 
 * @author Alexey Daninilov
 * 
 */
public class MethodInvocationHandler implements InvocationHandler {

    private Object methodOwner;

    private Method targetMethod;

    private Method sourceMethod;

    public MethodInvocationHandler(Object methodOwner, Method sourceMethod, Method targetMethod) {
        this.methodOwner = methodOwner;
        this.targetMethod = targetMethod;
        this.sourceMethod = sourceMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(targetMethod.getName())) {
            sourceMethod.setAccessible(true);
            return sourceMethod.invoke(methodOwner, args);
        }
        // call only one target method, ignore other
        return null;
    }
}