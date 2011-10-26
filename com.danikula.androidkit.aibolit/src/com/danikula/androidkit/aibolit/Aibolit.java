package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.view.View;

import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnItemClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnLongClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;
import com.danikula.androidkit.aibolit.annotation.InjectView;
import com.danikula.androidkit.aibolit.injector.InjectorOnClickListener;
import com.danikula.androidkit.aibolit.injector.InjectorOnItemClickListener;
import com.danikula.androidkit.aibolit.injector.InjectorOnLongClickListener;
import com.danikula.androidkit.aibolit.injector.InjectorOnTouchListener;
import com.danikula.androidkit.aibolit.injector.InjectorVew;

public class Aibolit {

    private static final Map<Class<? extends Annotation>, FieldInjector<?>> FIELD_INJECTORS_REGISTER;

    private static final Map<Class<? extends Annotation>, MethodInjector<?>> METHOD_INJECTORS_REGISTER;

    static {
        FIELD_INJECTORS_REGISTER = new HashMap<Class<? extends Annotation>, FieldInjector<?>>();
        METHOD_INJECTORS_REGISTER = new HashMap<Class<? extends Annotation>, MethodInjector<?>>();

        FIELD_INJECTORS_REGISTER.put(InjectView.class, new InjectorVew());

        METHOD_INJECTORS_REGISTER.put(InjectOnClickListener.class, new InjectorOnClickListener());
        METHOD_INJECTORS_REGISTER.put(InjectOnLongClickListener.class, new InjectorOnLongClickListener());
        METHOD_INJECTORS_REGISTER.put(InjectOnItemClickListener.class, new InjectorOnItemClickListener());
        METHOD_INJECTORS_REGISTER.put(InjectOnTouchListener.class, new InjectorOnTouchListener());
    }

    public static void doInjections(Object holder, View view) {
        Validate.notNull(holder, "Can't inject in null object");
        Validate.notNull(view, "Can't process null view");

        Class<?> holderClass = holder.getClass();
        injectFields(holder, view, holderClass.getDeclaredFields());
        injectMethods(holder, view, holderClass.getDeclaredMethods());
    }
    
    public static void doInjections(Activity activity) {
        doInjections(activity, activity.getWindow().getDecorView());
    }

    public static void doInjections(View view) {
        doInjections(view, view);
    }

    public static void doInjections(Object holder, Activity activity) {
        doInjections(holder, activity.getWindow().getDecorView());
    }

    private static void injectFields(Object holder, View view, Field[] fields) {
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (FIELD_INJECTORS_REGISTER.containsKey(annotationClass)) {
                    FieldInjector<Annotation> injector = getFieldInjector(annotationClass);
                    injector.doInjection(holder, view, field, annotation);
                }
            }
        }
    }

    private static void injectMethods(Object holder, View view, Method[] methods) {
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (METHOD_INJECTORS_REGISTER.containsKey(annotationClass)) {
                    MethodInjector<Annotation> injector = getMethodInjector(annotationClass);
                    injector.doInjection(holder, view, method, annotation);
                }
            }
        }
    }

    private static FieldInjector<Annotation> getFieldInjector(Class<? extends Annotation> annotationClass) {
        return (FieldInjector<Annotation>) FIELD_INJECTORS_REGISTER.get(annotationClass);
    }

    private static MethodInjector<Annotation> getMethodInjector(Class<? extends Annotation> annotationClass) {
        return (MethodInjector<Annotation>) METHOD_INJECTORS_REGISTER.get(annotationClass);
    }

}