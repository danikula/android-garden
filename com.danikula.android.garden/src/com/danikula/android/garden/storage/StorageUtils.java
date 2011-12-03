package com.danikula.android.garden.storage;

import java.io.File;

import com.danikula.android.garden.utils.StringUtils;

import android.content.Context;
import android.os.Environment;

public class StorageUtils {

    private static final String RECOMENDED_APPLICATION_FOLDER = "%s/Android/data/%s/files/%s";

    public static String getRecomendedExternalStorageDirecory(Context context, String additionalStorageFolder) {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String storageDir = StringUtils.getStringNullSafe(additionalStorageFolder);
        return String.format(RECOMENDED_APPLICATION_FOLDER, sdPath, context.getPackageName(), storageDir);
    }

    public static String getTempStorageDirecory(Context context, String additionalStorageFolder) {
        String storageDir = StringUtils.getStringNullSafe(additionalStorageFolder);
        return StringUtils.join(context.getCacheDir().getAbsolutePath(), File.separator, storageDir);
    }
}
