package com.danikula.androidkit.aibolit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.danikula.androidkit.aibolit.injector.AbstractInjector;
import com.danikula.androidkit.aibolit.injector.AbstractMethodInjector;
import com.danikula.androidkit.aibolit.injector.InjectorRegister;

/**
 * Does injections into object.
 * <p>
 * Class can inject:
 * <ul>
 * <li>Views annotated by {@link InjectView}</li>
 * <li>Application resources (drawable, string, anim, layout, bool, dimen, integer, array, color) annotated by
 * {@link InjectResource}.</li>
 * <li>ArrayAdapter annotated by {@link InjectArrayAdapter}</li>
 * <li>System services annotated by {@link InjectSystemService}</li>
 * <li>Custom application services annotated by {@link InjectService} and resolved with help {@link InjectionResolver}</li>
 * <li>Events handlers annotated by:
 * <ul>
 * <li> {@link InjectOnCheckedChangeListener}</li>
 * <li> {@link InjectOnClickListener}</li>
 * <li> {@link InjectOnCreateContextMenuListener}</li>
 * <li> {@link InjectOnEditorActionListener}</li>
 * <li> {@link InjectOnFocusChangeListener}</li>
 * <li> {@link InjectOnItemClickListener}</li>
 * <li> {@link InjectOnItemSelectedListener}</li>
 * <li> {@link InjectOnKeyListener}</li>
 * <li> {@link InjectOnLongClickListener}</li>
 * <li> {@link InjectOnRadioGroupCheckedChangeListener}</li>
 * <li> {@link InjectOnTextChangedListener}</li>
 * <li> {@link InjectOnTouchListener}</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * Typical usage :
 * 
 * <pre>
 * public class AibolitChatActivity extends Activity {
 * 
 *     // annotate injected fileds ...
 *     
 *     &#064;InjectView(R.id.messageEditText)
 *     private EditText messageEditText;
 * 
 *     &#064;InjectView(R.id.historyListView)
 *     private ListView historyListView;
 * 
 *     &#064;InjectResource(R.string.symbols_count)
 *     private String symbolsCountPattern;
 *     
 *     ...
 * 
 *     &#064;Override
 *     public void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 * 
 *         setContentView(R.layout.chat_activity);
 *         // initialize annotated fields and methods
 *         Aibolit.doInjections(this);
 *         
 *         // or just Aibolit.setInjectedContentView(this);
 *         
 *         ...
 *     }
 * 
 *     // annotate event handlers... 
 *     
 *     &#064;InjectOnClickListener(R.id.sendButton)
 *     private void onSendButtonClick(View v) {
 *         String text = messageEditText.getText().toString();
 *         // do work with text
 *     }
 * 
 *     &#064;InjectOnClickListener(R.id.clearHistoryButton)
 *     private void onClearHistoryButtonClick(View v) {
 *         // handle button click
 *     }
 * 
 *     &#064;InjectOnTextChangedListener(R.id.messageEditText)
 *     public void onMessageTextChanged(CharSequence s, int start, int before, int count) {
 *         // handle text changed event
 *     }
 *     
 *     ...
 * 
 * }
 * </pre>
 * 
 * If superclass also should be injected just annotate your class with {@link AibolitSettings} annotattion with parameter
 * {@link AibolitSettings#injectSuperclasses()} <code>true</code>
 * 
 * @author Alexey Danilov
 * 
 */
public class Aibolit {

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
        InjectorRegister.addInjectionResolver(injectionResolver);
    }

    private static ArrayList<Field> getFieldsList(Class<?> classToInspect, boolean includeSuperclassFields) {
        ArrayList<Field> fieldsList = new ArrayList<Field>(Arrays.asList(classToInspect.getDeclaredFields()));
        if (includeSuperclassFields && classToInspect.getSuperclass() != null) {
            fieldsList.addAll(getFieldsList(classToInspect.getSuperclass(), includeSuperclassFields));
        }
        return fieldsList;
    }

    private static ArrayList<Method> getMethodsList(Class<?> classToInspect, boolean includeSuperclassFields) {
        ArrayList<Method> methodsList = new ArrayList<Method>(Arrays.asList(classToInspect.getDeclaredMethods()));
        if (includeSuperclassFields && classToInspect.getSuperclass() != null) {
            methodsList.addAll(getMethodsList(classToInspect.getSuperclass(), includeSuperclassFields));
        }
        return methodsList;
    }

    private static void injectFields(Object holder, View view, List<Field> fields) {
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (InjectorRegister.contains(annotationClass)) {
                    AbstractFieldInjector<Annotation> injector = InjectorRegister.getFieldInjector(annotationClass);
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
                if (InjectorRegister.contains(annotationClass)) {
                    AbstractMethodInjector<Annotation> injector = InjectorRegister.getMethodInjector(annotationClass);
                    injector.doInjection(holder, view, method, annotation);
                }
            }
        }
    }
}