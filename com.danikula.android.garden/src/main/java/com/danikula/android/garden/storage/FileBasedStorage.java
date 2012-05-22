package com.danikula.android.garden.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import com.danikula.android.garden.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import android.graphics.Paint.Join;
import android.os.Environment;

public abstract class FileBasedStorage<T> implements Storage<String, T> {

    private static final String LOG_TAG = FileBasedStorage.class.getSimpleName();

    private static final String DEFAULT_EXTENSION = "bin";

    private File storageDir;

    private String fileExtension;

    public FileBasedStorage(String storagePath) {
        this(storagePath, DEFAULT_EXTENSION);
    }

    public FileBasedStorage(String storagePath, String fileExtension) {
        this.storageDir = new File(checkNotNull(storagePath));
        this.fileExtension = checkNotNull(fileExtension, "File extension must be not null!");
    }

    @Override
    public void put(String key, T value) throws StorageException {
        checkNotNull(key, "Key must be not null!");
        checkNotNull(key, "Value must be not null!");

        if (!storageDir.canWrite()) {
            boolean created = storageDir.mkdirs();
            if (!created) {
                String errorMsgFormat = "Error creating cache directory '%s'. External storage state: %s";
                String error = String.format(errorMsgFormat, storageDir.getAbsolutePath(), Environment.getExternalStorageState());
                throw new StorageException(error);
            }
        }

        File storageFile = getStorageFile(key);
        try {
            write(storageFile, value);
        }
        catch (IOException e) {
            throw new StorageException("Error saving item to cache storage");
        }
    }

    @Override
    public T get(String key) throws StorageException {
        if (!contains(key)) {
            String errorMessageFormat = "Error reading cache item '%s'. Use method contains(String) to check exisiting cache item";
            throw new StorageException(String.format(errorMessageFormat, key));
        }

        File storageFile = getStorageFile(key);
        T result = null;
        try {
            result = read(storageFile);
        }
        catch (Exception e) {
            String errorMessageFormat = "Error reading cache item with key '%s' and path '%s' from cache storage";
            throw new StorageException(String.format(errorMessageFormat, key, storageFile.getAbsolutePath()));
        }
        return result;
    }

    @Override
    public boolean contains(String key) {
        return getStorageFile(key).canRead();
    }

    @Override
    public void clear() throws StorageException {
        if (!storageDir.exists()) {
            return;
        }
        for (File storageFile : storageDir.listFiles()) {
            boolean isDeleted = storageFile.delete();
            if (!isDeleted) {
                String errorMessageFormat = "Error deleting cache item '%s' from cache storage";
                throw new StorageException(String.format(errorMessageFormat, storageFile.getAbsolutePath()));
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
            throw new StorageException(String.format(errorMessageFormat, key, storageFile.getAbsolutePath()));
        }
    }

    protected abstract void write(File file, T value) throws IOException;

    protected abstract T read(File file) throws IOException;

    private File getStorageFile(String key) {
        String fileName = String.format("%s.%s", StringUtils.computeMD5(key), fileExtension);
        return new File(storageDir, fileName);
    }
}
