package com.danikula.androidkit.aibolit;

/**
 * Resolves custom application service
 * 
 * @author Alexey Danilov
 * 
 */
public interface InjectionResolver {

    /**
     * Resolves application service by class
     * 
     * @param serviceClass Class service's class
     * @return Object application service
     */
    Object resolve(Class<?> serviceClass);

}
