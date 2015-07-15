package com.danikula.android.garden.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class Version {

    private static final int BUILD_VERSION_CODE_ICE_CREAM_SANDWICH_MR1 = 15;
    private static final int BUILD_VERSION_CODE_JELLY_BEAN = 16;
    private static final int BUILD_VERSION_CODE_JELLY_BEAN_MR1 = 17;
    private static final int BUILD_VERSION_CODE_JELLY_BEAN_MR2 = 18;
    private static final int BUILD_VERSION_CODE_KITKAT = 19;
    private static final int BUILD_VERSION_CODE_KITKAT_WATCH = 20;
    private static final int BUILD_VERSION_CODE_LOLLIPOP = 21;
    private static final int BUILD_VERSION_CODE_LOLLIPOP_MR1 = 22;

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasGingerbreadMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasHoneycombMr2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasIceCreamSandwichMr1() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_JELLY_BEAN;
    }

    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMr2() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_JELLY_BEAN_MR2;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_KITKAT;
    }

    public static boolean hasKitkatWatch() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_KITKAT_WATCH;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_LOLLIPOP;
    }

    public static boolean hasLollipopMr1() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_LOLLIPOP_MR1;
    }

    public static boolean hasApi8() {
        return hasFroyo();
    }

    public static boolean hasApi9() {
        return hasGingerbread();
    }

    public static boolean hasApi10() {
        return hasGingerbreadMr1();
    }

    public static boolean hasApi11() {
        return hasHoneycomb();
    }

    public static boolean hasApi12() {
        return hasHoneycombMr1();
    }

    public static boolean hasApi13() {
        return hasHoneycombMr2();
    }

    public static boolean hasApi14() {
        return hasIceCreamSandwich();
    }

    public static boolean hasApi15() {
        return hasIceCreamSandwichMr1();
    }

    public static boolean hasApi16() {
        return hasJellyBean();
    }

    public static boolean hasApi17() {
        return hasJellyBeanMr1();
    }

    public static boolean hasApi18() {
        return hasJellyBeanMr2();
    }

    public static boolean hasApi19() {
        return hasKitkat();
    }

    public static boolean hasApi20() {
        return hasKitkatWatch();
    }

    public static boolean hasApi21() {
        return hasLollipop();
    }

    public static boolean hasApi22() {
        return hasLollipopMr1();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }


}
