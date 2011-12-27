package com.danikula.android.garden.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;

import com.danikula.android.garden.io.IoUtils;

public class AndroidUtils {
    
    private static final String APP_EXTERNAL_STORAGE_DIR_FORMAT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/%s/files/";

    public static String getApplicationExternalStorageDirectory(Context context) {
        return String.format(APP_EXTERNAL_STORAGE_DIR_FORMAT, context.getPackageName());
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

}
