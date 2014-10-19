package com.danikula.android.garden.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.danikula.android.garden.io.IoUtils;

public class AndroidUtils {

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
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Can't load application properties"));
        } finally {
            IoUtils.closeSilently(inputStream);
        }
    }

    /**
     * Checks is device has access to Internet at the moment.
     * <p>
     * Permission "android.permission.ACCESS_NETWORK_STATE" is required
     * </p>
     * 
     * @param context Context an android context
     * @return <code>true</code> if device has access to Internet at the moment
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getApplicationVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getApplicationVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            String packageName = context.getPackageName();
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            throw new IllegalStateException("Error retrieving package info", e);
        }
    }

}
