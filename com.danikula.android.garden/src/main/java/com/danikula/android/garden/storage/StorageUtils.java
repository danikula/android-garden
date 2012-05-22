package com.danikula.android.garden.storage;

import java.io.File;

import com.google.common.base.Strings;

import android.content.Context;
import android.os.Environment;

public class StorageUtils {

    private static final String RECOMENDED_APPLICATION_FOLDER = "%s/Android/data/%s/files";

    public static File getRecomendedExternalStorageDirectory(Context context, String additionalStorageFolder) {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String storageDir = Strings.nullToEmpty(additionalStorageFolder);
        return new File(String.format(RECOMENDED_APPLICATION_FOLDER, sdPath, context.getPackageName(), storageDir));
    }

    public static File getTempStorageDirectory(Context context, String additionalStorageFolder) {
        String storageSubDir = Strings.nullToEmpty(additionalStorageFolder);
        return new File(context.getCacheDir(), storageSubDir);
    }
}
