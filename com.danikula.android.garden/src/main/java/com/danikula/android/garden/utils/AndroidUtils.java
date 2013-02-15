package com.danikula.android.garden.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import com.danikula.android.garden.io.IoUtils;

public class AndroidUtils {

    private static final int BUILD_VERSION_CODE_FROYO = 8;
    private static final int BUILD_VERSION_CODE_GINGERBREAD = 9;
    private static final int BUILD_VERSION_CODE_HONEYCOMB = 11;
    private static final int BUILD_VERSION_CODE_HONEYCOMB_MR1 = 12;
    private static final int BUILD_VERSION_CODE_ICE_CREAM_SANDWICH = 14;
    private static final int BUILD_VERSION_CODE_JELLY_BEAN = 16;

    private static final String APP_EXTERNAL_STORAGE_DIR_FORMAT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/%s/files/";

    public static String getApplicationExternalStorageDirectory(Context context) {
        return String.format(APP_EXTERNAL_STORAGE_DIR_FORMAT, context.getPackageName());
    }

    public static File getApplicationExternalStorageSubDirectory(Context context, String subfolderName) {
        return new File(getApplicationExternalStorageDirectory(context), subfolderName);
    }

    public static Properties loadPropertiesFromAssets(Context context, String propertiesFileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(propertiesFileName);

            Properties properties = new Properties();
            properties.load(inputStream);

            return properties;
        }
        catch (IOException e) {
            throw new IllegalStateException(String.format("Can't load application properties"));
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
    }

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
    
    /**
     * Checks is device has access to Internet at the moment. 
     * <p>Permission "android.permission.ACCESS_NETWORK_STATE" is required</p>
     * @param context Context an android context
     * @return <code>true</code> if device has access to Internet at the moment 
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
