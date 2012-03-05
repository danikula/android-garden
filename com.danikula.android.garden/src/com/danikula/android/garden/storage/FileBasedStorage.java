package com.danikula.android.garden.storage;

import java.io.File;
import java.io.IOException;

import com.danikula.android.garden.utils.StringUtils;
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
    public void put(String key, T value) throws StorageException {
        Validate.notNull(key, "key");
        Validate.notNull(value, "value");

        if (!storageDirecory.canWrite()) {
            boolean created = storageDirecory.mkdirs();
            if (!created) {
                throw new StorageException("Error creating cache directory");
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
        if (!storageDirecory.exists()) {
            return;
        }
        for (File storageFile : storageDirecory.listFiles()) {
            boolean isDeleted = storageFile.delete();
            if (!isDeleted) {
                String errorMessageFormat = "Error deleting cache item '%s' from cache storage";
                throw new StorageException(String.format(errorMessageFormat, storageFile.getAbsolutePath()));
            }
        }
        storageDirecory.delete();
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
        String fileName = StringUtils.join(StringUtils.computeMD5(key), ".", fileExtension);
        return new File(storageDirecory, fileName);
    }
}
