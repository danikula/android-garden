package com.danikula.androidkit.aibolit.injector;

import java.lang.reflect.Field;
import java.util.List;


import com.danikula.androidkit.aibolit.Aibolit;
import com.danikula.androidkit.aibolit.InjectingException;
import com.danikula.androidkit.aibolit.InjectionContext;
import com.danikula.androidkit.aibolit.ServicesResolver;
import com.danikula.androidkit.aibolit.annotation.InjectService;

/**
 * Injects application service. Client code have to add custom {@link ServicesResolver} with help method
 * {@link Aibolit#addInjectionResolver(ServicesResolver)}
 * 
 * @author Alexey Danilov
 * 
 */
/* package private */class ServiceInjector extends AbstractFieldInjector<InjectService> {

    private List<ServicesResolver> resolvers;

    public ServiceInjector(List<ServicesResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public void doInjection(Object fieldOwner, InjectionContext injectionContext, Field field, InjectService annotation) {
        Object service = null;
        Class<?> serviceClass = field.getType();
        for (ServicesResolver injectionResolver : resolvers) {
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
