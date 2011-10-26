package com.danikula.androidkit.aibolit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {

    int value();

}
