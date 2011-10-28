package com.danikula.androidkit.aibolit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

import com.danikula.androidkit.aibolit.Aibolit;

/**
 * Anotation is used for injecting {@link View.OnKeyListener#onKey(View, int, android.view.KeyEvent)} method for specified view.
 * See docs for {@link Aibolit} for more information.
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#064;InjectOnKeyListener(R.id.editText)
 * private boolean onKey(View v, int keyCode, KeyEvent event) {
 *     // process key event
 *     return false;
 * }
 * </pre>
 * 
 * </p>
 * 
 * @see Aibolit
 * @see View.OnKeyListener
 * 
 * @author Alexey Danilov
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectOnKeyListener {

    /**
     * Returns identifier of view to be used for setting listener
     * 
     * @return int view id
     */
    int value();

}
