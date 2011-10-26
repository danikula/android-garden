package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Field;
import java.util.List;

import android.view.View;

import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.InjectionResolver;
import com.danikula.androidkit.aibolit.annotation.InjectService;

public class ServiceInjector extends AbstractFieldInjector<InjectService> {
    
    private List<InjectionResolver> resolvers;
    
    public ServiceInjector(List<InjectionResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public void doInjection(Object fieldOwner, View viewHolder, Field field, InjectService annotation) {
        Object service = null;
        Class<?> serviceClass = field.getType();
        for (InjectionResolver injectionResolver : resolvers) {
            service = injectionResolver.resolve(serviceClass);
            if (service != null) {
                break;
            }
        }
        if (service == null) {
            String errorPattern = "There is no registered service for field named '%s' with type %s";
            throw new InjectingException(String.format(errorPattern, field.getName(), serviceClass.getName()));
        }
        
        setValue(fieldOwner, field, service);
    }
}
