package com.danikula.android.garden.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import com.danikula.android.garden.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import android.graphics.Paint.Join;
import android.os.Environment;

public abstract class FileBasedCache<T> implements Cache<String, T> {

    private static final String LOG_TAG = FileBasedCache.class.getSimpleName();

    private static final String DEFAULT_EXTENSION = "bin";

    private File storageDir;

    private String fileExtension;

    public FileBasedCache(String storagePath) {
        this(storagePath, DEFAULT_EXTENSION);
    }

    public FileBasedCache(String storagePath, String fileExtension) {
        this.storageDir = new File(checkNotNull(storagePath));
        this.fileExtension = checkNotNull(fileExtension, "File extension must be not null!");
    }

    @Override
    public void put(String key, T value) throws CacheException {
        checkNotNull(key, "Key must be not null!");
        checkNotNull(key, "Value must be not null!");

        if (!storageDir.canWrite()) {
            boolean created = storageDir.mkdirs();
            if (!created) {
                String errorMsgFormat = "Error creating cache directory '%s'. External storage state: %s";
                String error = String.format(errorMsgFormat, storageDir.getAbsolutePath(), Environment.getExternalStorageState());
                throw new CacheException(error);
            }
        }

        File storageFile = getStorageFile(key);
        try {
            write(storageFile, value);
        }
        catch (IOException e) {
            throw new CacheException("Error saving item to cache storage");
        }
    }

    @Override
    public T get(String key) throws CacheException {
        if (!contains(key)) {
            String errorMessageFormat = "Error reading cache item '%s'. Use method contains(String) to check exisiting cache item";
            throw new CacheException(String.format(errorMessageFormat, key));
        }

        File storageFile = getStorageFile(key);
        T result = null;
        try {
            result = read(storageFile);
        }
        catch (Exception e) {
            String errorMessageFormat = "Error reading cache item with key '%s' and path '%s' from cache storage";
            throw new CacheException(String.format(errorMessageFormat, key, storageFile.getAbsolutePath()));
        }
        return result;
    }

    @Override
    public boolean contains(String key) {
        return getStorageFile(key).canRead();
    }

    @Override
    public void clear() throws CacheException {
        if (!storageDir.exists()) {
            return;
        }
        for (File storageFile : storageDir.listFiles()) {
            boolean isDeleted = storageFile.delete();
            if (!isDeleted) {
                String errorMessageFormat = "Error deleting cache item '%s' from cache storage";
                throw new CacheException(String.format(errorMessageFormat, storageFile.getAbsolutePath()));
            }
        }
        storageDir.delete();
    }

    @Override
    public void remove(String key) {
        File storageFile = getStorageFile(key);
        boolean isDeleted = storageFile.delete();
        if (!isDeleted) {
            String errorMessageFormat = "Error deleting cache item with key '%s' and path '%s' from cache storage";
            throw new CacheException(String.format(errorMessageFormat, key, storageFile.getAbsolutePath()));
        }
    }

    protected abstract void write(File file, T value) throws IOException;

    protected abstract T read(File file) throws IOException;

    private File getStorageFile(String key) {
        String fileName = String.format("%s.%s", StringUtils.computeMD5(key), fileExtension);
        return new File(storageDir, fileName);
    }
}
