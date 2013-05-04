package com.danikula.android.garden.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

import com.danikula.android.garden.utils.ReflectUtils;
import com.google.common.base.Preconditions;

/**
 * Содержит ряд полезных методов для работы с потоками.
 * 
 * @author danik
 */
public class IoUtils {

    private static final String STREAM_ENCODING = "UTF-8";

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private static final String LOG_TAG = IoUtils.class.getName();

    /**
     * @param inputStream читает поток символов из входящего потока, используя кодировку UTF и формирует из него строку. Входящий
     *            поток не закрывается.
     * @return String содержимое входящего потока как строку
     * @throws IOException если возникла проблема чтения потока
     */
    public static String streamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, STREAM_ENCODING), DEFAULT_BUFFER_SIZE);
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            return str.toString();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Closes stream and releases any system resources associated with it.
     * <p>
     * Hide {@link IOException} if it occurs during closing resource
     * </p>
     * 
     * @param closeableSource Closeable source to be needed to close
     */
    public static void closeSilently(Closeable closeableSource) {
        try {
            if (closeableSource != null) {
                closeableSource.close();
            }
        }
        catch (IOException e) {
            // hide exception, close source "silently"
            Log.e(LOG_TAG, "Error closing closeable source", e);
        }
    }

    /**
     * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
     * </p>
     * 
     * @param input the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @throws ArithmeticException if the byte count is too large
     * @since Commons IO 1.1
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        ProgressListener emptyListener = ReflectUtils.newInstance(ProgressListener.class);
        return copy(input, output, emptyListener);
    }

    /**
     * Saves data to file.
     * 
     * @param data byte data to be saved, must be not null.
     * @param targetFile file to be used for saving data, must be not null.
     * @throws IOException if an I/O error occurs
     */
    public static void saveToFile(byte[] data, File targetFile) throws IOException {
        Preconditions.checkNotNull(data, "Data must be not null!");
        Preconditions.checkNotNull(targetFile, "Target file must be not null!");

        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            outputStream.write(data);
        }
        finally {
            closeSilently(outputStream);
        }
    }
    
    /**
     * reads content of file.
     * 
     * @param sourceFile file to be used for reading data, must be not null.
     * @return content of file.
     * @throws IOException if an I/O error occurs
     */
    public static byte[] readFile(File sourceFile) throws IOException {
        Preconditions.checkNotNull(sourceFile, "Target file must be not null!");

        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            byte[] data = new byte[(int)sourceFile.length()];
            inputStream.read(data);
            return data;
        }
        finally {
            closeSilently(inputStream);
        }
    }

    public static long copy(InputStream input, OutputStream output, ProgressListener listener) throws IOException {
        Preconditions.checkNotNull(input, "Input stream must be not null!");
        Preconditions.checkNotNull(output, "Output stream must be not null!");
        Preconditions.checkNotNull(listener, "Progress listener must be not null!");

        InputStream bufferedInputStream = new BufferedInputStream(input);
        OutputStream bufferedOutputStream = new BufferedOutputStream(output);

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        listener.onProgress(0);
        while (-1 != (n = bufferedInputStream.read(buffer))) {
            bufferedOutputStream.write(buffer, 0, n);
            count += n;
            listener.onProgress(count);
        }
        bufferedOutputStream.flush();
        return count;
    }

    public interface ProgressListener {

        public void onProgress(long bytesCopied);
    }

    public abstract static class PeriodProgressListener implements ProgressListener {

        private long publishEveryBytes;

        private long publishCount = -1;

        public PeriodProgressListener(long publishEveryBytes) {
            this.publishEveryBytes = publishEveryBytes;
        }

        @Override
        public void onProgress(long bytesCopied) {
            long progress = bytesCopied / publishEveryBytes;
            if (progress > publishCount) {
                publishCount = progress;
                onPeriodProgress(bytesCopied);
            }
        }

        public abstract void onPeriodProgress(long bytesCopied);

    }

}
