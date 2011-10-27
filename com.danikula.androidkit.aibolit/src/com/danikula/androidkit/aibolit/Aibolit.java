package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.danikula.androidkit.aibolit.annotation.AibolitSettings;
import com.danikula.androidkit.aibolit.annotation.InjectArrayAdapter;
import com.danikula.androidkit.aibolit.annotation.InjectOnCheckedChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnCreateContextMenuListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnEditorActionListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnFocusChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnItemClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnItemSelectedListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnKeyListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnLongClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnRadioGroupCheckedChangeListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTextChangedListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTouchListener;
import com.danikula.androidkit.aibolit.annotation.InjectResource;
import com.danikula.androidkit.aibolit.annotation.InjectService;
import com.danikula.androidkit.aibolit.annotation.InjectSystemService;
import com.danikula.androidkit.aibolit.annotation.InjectView;
import com.danikula.androidkit.aibolit.injector.AbstractFieldInjector;
import com.danikula.androidkit.aibolit.injector.AbstractMethodInjector;
import com.danikula.androidkit.aibolit.injector.ArrayAdapterInjector;
import com.danikula.androidkit.aibolit.injector.OnCheckedChangeInjector;
import com.danikula.androidkit.aibolit.injector.OnClickListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnCreateContextMenuListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnEditorActionListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnFocusChangeListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnItemClickListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnItemSelectedListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnKeyListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnLongClickListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnRadioGroupCheckedChangeInjector;
import com.danikula.androidkit.aibolit.injector.OnTextChangedListenerInjector;
import com.danikula.androidkit.aibolit.injector.OnTouchListenerInjector;
import com.danikula.androidkit.aibolit.injector.ResourceInjector;
import com.danikula.androidkit.aibolit.injector.ServiceInjector;
import com.danikula.androidkit.aibolit.injector.SystemServiceInjector;
import com.danikula.androidkit.aibolit.injector.ViewInjector;

public class Aibolit {

    private static final Map<Class<? extends Annotation>, AbstractFieldInjector<?>> FIELD_INJECTORS_REGISTER;

    private static final Map<Class<? extends Annotation>, AbstractMethodInjector<?>> METHOD_INJECTORS_REGISTER;

    private static final List<InjectionResolver> INJECTION_RESOLVERS;

    static {
        INJECTION_RESOLVERS = new LinkedList<InjectionResolver>();
        FIELD_INJECTORS_REGISTER = new HashMap<Class<? extends Annotation>, AbstractFieldInjector<?>>();
        METHOD_INJECTORS_REGISTER = new HashMap<Class<? extends Annotation>, AbstractMethodInjector<?>>();

        FIELD_INJECTORS_REGISTER.put(InjectView.class, new ViewInjector());
        FIELD_INJECTORS_REGISTER.put(InjectResource.class, new ResourceInjector());
        FIELD_INJECTORS_REGISTER.put(InjectArrayAdapter.class, new ArrayAdapterInjector());
        FIELD_INJECTORS_REGISTER.put(InjectSystemService.class, new SystemServiceInjector());
        FIELD_INJECTORS_REGISTER.put(InjectService.class, new ServiceInjector(INJECTION_RESOLVERS));

        METHOD_INJECTORS_REGISTER.put(InjectOnClickListener.class, new OnClickListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnLongClickListener.class, new OnLongClickListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnItemClickListener.class, new OnItemClickListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnItemSelectedListener.class, new OnItemSelectedListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnTouchListener.class, new OnTouchListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnKeyListener.class, new OnKeyListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnFocusChangeListener.class, new OnFocusChangeListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnCreateContextMenuListener.class, new OnCreateContextMenuListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnTextChangedListener.class, new OnTextChangedListenerInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnCheckedChangeListener.class, new OnCheckedChangeInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnRadioGroupCheckedChangeListener.class, new OnRadioGroupCheckedChangeInjector());
        METHOD_INJECTORS_REGISTER.put(InjectOnEditorActionListener.class, new OnEditorActionListenerInjector());
    }

    public static void doInjections(Object holder, View view) {
        Validate.notNull(holder, "Can't inject in null object");
        Validate.notNull(view, "Can't process null view");

        Class<?> holderClass = holder.getClass();
        AibolitSettings aibolitSettings = holder.getClass().getAnnotation(AibolitSettings.class);
        boolean injectSuperclasses = aibolitSettings != null && aibolitSettings.injectSuperclasses();
        
        List<Field> fields = getFieldsList(holderClass, injectSuperclasses);
        injectFields(holder, view, fields);

        List<Method> methods = getMethodsList(holderClass, injectSuperclasses);
        injectMethods(holder, view, methods);
    }

    public static void doInjections(Activity activity) {
        doInjections(activity, activity.getWindow().getDecorView());
    }

    public static void doInjections(Dialog dialog) {
        doInjections(dialog, dialog.getWindow().getDecorView());
    }

    public static void doInjections(View view) {
        doInjections(view, view);
    }

    public static void doInjections(Object holder, Activity activity) {
        doInjections(holder, activity.getWindow().getDecorView());
    }

    public static void setInjectedContentView(Activity activity, int layoutId) {
        activity.setContentView(layoutId);
        doInjections(activity);
    }

    public static void setInjectedContentView(Activity activity, View contentView) {
        activity.setContentView(contentView);
        doInjections(activity);
    }

    public static void setInjectedContentView(Dialog dialog, int layoutId) {
        dialog.setContentView(layoutId);
        doInjections(dialog);
    }

    public static void setInjectedContentView(Dialog dialog, View contentView) {
        dialog.setContentView(contentView);
        doInjections(dialog);
    }

    public static void addInjectionResolver(InjectionResolver injectionResolver) {
        Validate.notNull(injectionResolver, "InjectionResolver must be not null");
        INJECTION_RESOLVERS.add(injectionResolver);
    }
    
    private static ArrayList<Field> getFieldsList(Class<?> classToInspect, boolean includeSuperclassFields) {
        ArrayList<Field> fieldsList = new ArrayList<Field>(Arrays.asList(classToInspect.getDeclaredFields()));
        if(includeSuperclassFields && classToInspect.getSuperclass() != null){
            fieldsList.addAll(getFieldsList(classToInspect.getSuperclass(), includeSuperclassFields));
        }
        return fieldsList;
    }
    
    private static ArrayList<Method> getMethodsList(Class<?> classToInspect, boolean includeSuperclassFields) {
        ArrayList<Method> methodsList = new ArrayList<Method>(Arrays.asList(classToInspect.getDeclaredMethods()));
        if(includeSuperclassFields && classToInspect.getSuperclass() != null){
            methodsList.addAll(getMethodsList(classToInspect.getSuperclass(), includeSuperclassFields));
        }
        return methodsList;
    }

    private static void injectFields(Object holder, View view, List<Field> fields) {
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (FIELD_INJECTORS_REGISTER.containsKey(annotationClass)) {
                    AbstractFieldInjector<Annotation> injector = getFieldInjector(annotationClass);
                    injector.doInjection(holder, view, field, annotation);
                }
            }
        }
    }

    private static void injectMethods(Object holder, View view, List<Method> methods) {
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (METHOD_INJECTORS_REGISTER.containsKey(annotationClass)) {
                    AbstractMethodInjector<Annotation> injector = getMethodInjector(annotationClass);
                    injector.doInjection(holder, view, method, annotation);
                }
            }
        }
    }

    private static AbstractFieldInjector<Annotation> getFieldInjector(Class<? extends Annotation> annotationClass) {
        return (AbstractFieldInjector<Annotation>) FIELD_INJECTORS_REGISTER.get(annotationClass);
    }

    private static AbstractMethodInjector<Annotation> getMethodInjector(Class<? extends Annotation> annotationClass) {
        return (AbstractMethodInjector<Annotation>) METHOD_INJECTORS_REGISTER.get(annotationClass);
    }

}