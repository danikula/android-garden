package com.danikula.android.garden.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.util.Log;

import com.danikula.android.garden.io.IoUtils;

public class DiscStorage extends FileBasedStorage <Serializable> {
    
    private static final String LOG_TAG = DiscStorage.class.getSimpleName();

    public DiscStorage(String storagePath) {
        super(storagePath);
    }
    
    public DiscStorage(String storagePath, String fileExtension) {
        super(storagePath, fileExtension);
    }

    @Override
    protected void write(File file, Serializable value) throws IOException {
        ObjectOutputStream outputStream = null;
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            outputStream = new ObjectOutputStream(bufferedOutputStream);
            outputStream.writeObject(value);
            outputStream.flush();
        }
        finally {
            IoUtils.closeSilently(outputStream);
        }
    }

    @Override
    protected Serializable read(File file) throws IOException{
        Serializable result = null;
        ObjectInputStream inputStream = null;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            inputStream = new ObjectInputStream(bufferedInputStream);
            result = (Serializable) inputStream.readObject();
        }
        catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "Error reading object from file storage", e);
            // something weird
            throw new IllegalStateException("Error reading object from file storage", e);
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
        return result;
    }
}
