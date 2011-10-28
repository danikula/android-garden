package com.danikula.androidkit.aibolit.injector;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.ServicesResolver;
import com.danikula.androidkit.aibolit.Validate;
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

public class InjectorRegister {
    private static final Map<Class<? extends Annotation>, AbstractInjector<?>> INJECTORS_REGISTER;

    private static final List<ServicesResolver> SERVICES_RESOLVERS;

    static {
        SERVICES_RESOLVERS = new LinkedList<ServicesResolver>();
        INJECTORS_REGISTER = new HashMap<Class<? extends Annotation>, AbstractInjector<?>>();

        INJECTORS_REGISTER.put(InjectView.class, new ViewInjector());
        INJECTORS_REGISTER.put(InjectResource.class, new ResourceInjector());
        INJECTORS_REGISTER.put(InjectArrayAdapter.class, new ArrayAdapterInjector());
        INJECTORS_REGISTER.put(InjectSystemService.class, new SystemServiceInjector());
        INJECTORS_REGISTER.put(InjectService.class, new ServiceInjector(SERVICES_RESOLVERS));

        INJECTORS_REGISTER.put(InjectOnClickListener.class, new OnClickListenerInjector());
        INJECTORS_REGISTER.put(InjectOnLongClickListener.class, new OnLongClickListenerInjector());
        INJECTORS_REGISTER.put(InjectOnItemClickListener.class, new OnItemClickListenerInjector());
        INJECTORS_REGISTER.put(InjectOnItemSelectedListener.class, new OnItemSelectedListenerInjector());
        INJECTORS_REGISTER.put(InjectOnTouchListener.class, new OnTouchListenerInjector());
        INJECTORS_REGISTER.put(InjectOnKeyListener.class, new OnKeyListenerInjector());
        INJECTORS_REGISTER.put(InjectOnFocusChangeListener.class, new OnFocusChangeListenerInjector());
        INJECTORS_REGISTER.put(InjectOnCreateContextMenuListener.class, new OnCreateContextMenuListenerInjector());
        INJECTORS_REGISTER.put(InjectOnTextChangedListener.class, new OnTextChangedListenerInjector());
        INJECTORS_REGISTER.put(InjectOnCheckedChangeListener.class, new OnCheckedChangeInjector());
        INJECTORS_REGISTER.put(InjectOnRadioGroupCheckedChangeListener.class, new OnRadioGroupCheckedChangeInjector());
        INJECTORS_REGISTER.put(InjectOnEditorActionListener.class, new OnEditorActionListenerInjector());
    }

    public static boolean contains(Class<? extends Annotation> annotationClass) {
        return INJECTORS_REGISTER.containsKey(annotationClass);
    }

    public static AbstractFieldInjector<Annotation> getFieldInjector(Class<? extends Annotation> annotationClass) {
        AbstractInjector<?> abstractInjector = INJECTORS_REGISTER.get(annotationClass);
        if (!(abstractInjector instanceof AbstractFieldInjector)){
            throw new InjectingException("There is no registered AbstractFieldInjector for annotation class "  + annotationClass.getName());
        }
        return (AbstractFieldInjector<Annotation>) abstractInjector;
    }
    
    public static AbstractMethodInjector<Annotation> getMethodInjector(Class<? extends Annotation> annotationClass) {
        AbstractInjector<?> abstractInjector = INJECTORS_REGISTER.get(annotationClass);
        if (!(abstractInjector instanceof AbstractMethodInjector)){
            throw new InjectingException("There is no registered AbstractMethodInjector for annotation class "  + annotationClass.getName());
        }
        return (AbstractMethodInjector<Annotation>) abstractInjector;
    }
    
    public static void addServicesResolver(ServicesResolver serviceResolver){
        Validate.notNull(serviceResolver, "InjectionResolver must be not null");
        SERVICES_RESOLVERS.add(serviceResolver);
    }
}
