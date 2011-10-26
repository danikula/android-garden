package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.view.View;

public interface FieldInjector<A extends Annotation> {
    
    void doInjection(Object fieldOwner, View viewHolder, Field field, A annotation);
    
}
