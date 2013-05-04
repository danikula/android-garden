package com.danikula.android.garden.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;

public class UiUtils {

    /**
     * Скрывает виртуальную клавиатуру.
     * 
     * @param context контекст выполнения
     * @param editText компоненты {@link EditText}, для которых необходимо скрыть клавиатуру
     */
    public static void hideKeyboard(Context context, EditText... editTexts) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (EditText editText : editTexts) {
            inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * Sets visibility of views. {@link View#INVISIBLE} is used for hiding views.
     * 
     * @param visible boolean <code>true</code> if views should be visible.
     * @param views View[] views
     */
    public static void setVisibility(boolean visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * Sets visibility of views. {@link View#GONE} is used for hiding views.
     * 
     * @param visible boolean <code>true</code> if views should be visible.
     * @param views View[] views
     */
    public static void setVisibilityGone(boolean visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Скрывает TextView, если метод {@link TextView#getText()} возвращает <code>null</code> или пустую строку.
     * 
     * @param textView TextView компонент, который должен скрыться, если содержит пустой текст
     */
    public static void hideIfEmpty(TextView... textViews) {
        for (TextView textView : textViews) {
            String textContent = textView.getText().toString();
            setVisibility(!Strings.isNullOrEmpty(textContent), textView);
        }
    }

    public static int dipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
    }

    /**
     * Проверяет, открыта ли в данный момент виртуальная клавиатура.
     * 
     * @param activity Activity текущая активити
     * @return boolean <code>true</code> если в данный момент открыта виртуальная клавиатура.
     */
    public static boolean isKeyboardShown(Activity activity) {
        int displayHeight = activity.getWindowManager().getDefaultDisplay().getHeight();

        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int visiblePartHeight = rect.bottom;

        return displayHeight > visiblePartHeight;
    }

    /**
     * Создает view либо возвращает переданный в параметре view, если он не <code>null</code>.
     * 
     * <p>
     * Метод удобно использовать при переопределении метода {@link android.widget.Adapter#getView(int, View, ViewGroup)}
     * </p>
     * 
     * @param context Context android контекст
     * @param convertView View view, который можно реиспользовать, может быть <code>null</code>
     * @param layoutId идентификатор лейаута, используемый для создания нового view
     * @return View созданный view либо переданный в параметре convertView, если он не равен <code>null</code>
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public static View inflateUsingConvertView(Context context, View convertView, int layoutId) {
        return convertView != null ? convertView : LayoutInflater.from(context).inflate(layoutId, null);
    }

    public static boolean isPortraitOrientation(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscapeOrientation(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // @formatter:off
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    // @formatter:on

    /**
     * Returns width of screen in pixels.
     * 
     * @param activity activity to be used for retrieving info about display, must be not null.
     * @return width in pixels.
     */
    public static int getScreenWidthInPixels(Activity activity) {
        return getDisplayMetrics(activity).widthPixels;
    }

    /**
     * Returns display info.
     * 
     * @param activity activity to be used for retrieving info about display, must be not null.
     * @return display metrics.
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displaymetrics);
        return displaymetrics;
    }
}
