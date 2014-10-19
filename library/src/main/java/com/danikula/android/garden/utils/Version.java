package com.danikula.android.garden.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class Version {

    private static final int BUILD_VERSION_CODE_FROYO = 8;
    private static final int BUILD_VERSION_CODE_GINGERBREAD = 9;
    private static final int BUILD_VERSION_CODE_HONEYCOMB = 11;
    private static final int BUILD_VERSION_CODE_HONEYCOMB_MR1 = 12;
    private static final int BUILD_VERSION_CODE_ICE_CREAM_SANDWICH = 14;
    private static final int BUILD_VERSION_CODE_JELLY_BEAN = 16;
    
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= BUILD_VERSION_CODE_JELLY_BEAN;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }


}
