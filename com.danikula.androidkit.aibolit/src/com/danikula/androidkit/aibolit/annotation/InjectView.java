package com.danikula.androidkit.aibolit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.danikula.androidkit.aibolit.Aibolit;

/**
 * Anotation is used for injecting view into field. See docs for {@link Aibolit} for more information.
 * 
 * @see Aibolit
 * 
 * @author Alexey Danilov
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {

    /**
     * Returns identifier of view
     * 
     * @return int view id
     */
    int value();

}
