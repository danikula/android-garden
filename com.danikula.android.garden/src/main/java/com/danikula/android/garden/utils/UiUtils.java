package com.danikula.android.garden.utils;

import com.google.common.base.Strings;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UiUtils {

    /**
     * Скрывает виртуальную клавиатуру.
     * @param context Context контекст выполнения
     * @param editText EditText компонент {@link EditText}, для которого открыта клавиатура
     */
    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    
    /**
     * Sets visibility of views.
     * @param visible boolean <code>true</code> if views should be visible
     * @param views View[] views
     */
    public static void setVisibility(boolean visible, View...views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * Скрывает TextView, если метод {@link TextView#getText()} возвращает <code>null</code> или пустую строку.
     * @param textView TextView компонент, который должен скрыться, если содержит пустой текст
     */
    public static void hideIfEmpty(TextView textView) {
        String textContent = textView.getText().toString();
        setVisibility(!Strings.isNullOrEmpty(textContent), textView);
    }
    
}
