package com.danikula.android16.core.storage;

import java.io.File;

import com.danikula.android16.core.utils.TextUtils;

import android.content.Context;
import android.os.Environment;

public class StorageUtils {

    private static final String RECOMENDED_APPLICATION_FOLDER = "%s/Android/data/%s/files/%s";

    public static String getRecomendedExternalStorageDirecory(Context context, String additionalStorageFolder) {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String storageDir = TextUtils.getStringNullSafe(additionalStorageFolder);
        return String.format(RECOMENDED_APPLICATION_FOLDER, sdPath, context.getPackageName(), storageDir);
    }

    public static String getTempStorageDirecory(Context context, String additionalStorageFolder) {
        String storageDir = TextUtils.getStringNullSafe(additionalStorageFolder);
        return TextUtils.join(context.getCacheDir().getAbsolutePath(), File.separator, storageDir);
    }
}
