package com.danikula.android.garden.storage;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import com.danikula.android.garden.utils.TextUtils;
import com.danikula.android.garden.utils.Validate;

public abstract class FileBasedStorage<T> implements Storage<String, T> {

    private static final String LOG_TAG = FileBasedStorage.class.getSimpleName();

    private static final String DEFAULT_EXTENSION = "bin";

    private File storageDirecory;

    private String fileExtension;

    public FileBasedStorage(String storagePath) {
        this(storagePath, DEFAULT_EXTENSION);
    }

    public FileBasedStorage(String storagePath, String fileExtension) {
        Validate.notNull(storagePath, "storage directory");
        Validate.notNull(fileExtension, "extension");
        this.storageDirecory = new File(storagePath);
        this.fileExtension = fileExtension;
    }

    @Override
    public synchronized void put(String key, T value) {
        Validate.notNull(key, "key");
        Validate.notNull(value, "value");

        if (!storageDirecory.canWrite()) {
            boolean created = storageDirecory.mkdirs();
            if (!created) {
                return;
            }
        }

        File storageFile = getStorageFile(key);
        try {
            write(storageFile, value);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error saving object in file storage", e);
            // do not throw any exception, just log
        }
    }

    @Override
    public synchronized T get(String key) {
        if (!contains(key)) {
            return null;
        }

        File storageFile = getStorageFile(key);
        T result = null;
        try {
            result = read(storageFile);
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error reading object from file storage", e);
            // do not throw any exception, just log ant return null
        }
        return result;
    }

    @Override
    public boolean contains(String key) {
        return getStorageFile(key).canRead();
    }

    @Override
    public void clear() {
        if (!storageDirecory.exists()) {
            return;
        }
        for (File storageFile : storageDirecory.listFiles()) {
            storageFile.delete();
        }
        storageDirecory.delete();
    }

    protected abstract void write(File file, T value) throws IOException;

    protected abstract T read(File file) throws IOException;

    private File getStorageFile(String key) {
        String fileName = TextUtils.join(TextUtils.computeMD5(key), ".", fileExtension);
        return new File(storageDirecory, fileName);
    }
}
