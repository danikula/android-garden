package com.danikula.android.garden.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtils {

    public static <T> T newInstance(Class<T> instanceType) {
        ClassLoader classLoader = instanceType.getClassLoader();
        Class<?>[] implementedInterfaces = new Class[] { instanceType };
        return (T) Proxy.newProxyInstance(classLoader, implementedInterfaces, new EmptyInvokationProxy());
    }

    public static String[] getStringConstantValues(Class<?> clazz) {
        List<String> stringValues = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            boolean isStaticFinal = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
            boolean isString = field.getType().isAssignableFrom(String.class);
            if (isStaticFinal && isString) {
                String value = (String) getFieldValue(field, null);
                stringValues.add(value);
            }
        }
        Class<?>[] supportedInterfaces = clazz.getInterfaces();
        for (Class<?> supportedInterface : supportedInterfaces) {
            String[] interfaceConstantValues = getStringConstantValues(supportedInterface);
            stringValues.addAll(Arrays.asList(interfaceConstantValues));
        }
        Class<?> parentClass = clazz.getSuperclass();
        if (parentClass != null) {
            String[] parentConstantValues = getStringConstantValues(parentClass);
            stringValues.addAll(Arrays.asList(parentConstantValues));
        }
        return stringValues.toArray(new String[stringValues.size()]);
    }

    private static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error getting field's value", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("Error getting field's value", e);
        }
    }

    private static final class EmptyInvokationProxy implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

}
