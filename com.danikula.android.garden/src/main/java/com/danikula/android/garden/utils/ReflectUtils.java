package com.danikula.android.garden.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ReflectUtils {

    public static <T> T newInstance(Class<T> instanceType) {
        ClassLoader classLoader = instanceType.getClassLoader();
        Class[] implementedInterfaces = new Class[] { instanceType };
        return (T) Proxy.newProxyInstance(classLoader, implementedInterfaces, new EmptyInvokationProxy());
    }
    
    private static final class EmptyInvokationProxy implements InvocationHandler {
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
    
}
