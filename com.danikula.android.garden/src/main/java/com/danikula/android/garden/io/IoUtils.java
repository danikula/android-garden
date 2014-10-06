package com.danikula.android.garden.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.danikula.android.garden.utils.ReflectUtils;
import com.google.common.base.Preconditions;

import android.util.Log;

/**
 * Содержит ряд полезных методов для работы с потоками.
 * 
 * @author danik
 */
public class IoUtils {
    
    private static final String STREAM_ENCODING = "UTF-8";

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private static final String LOG_TAG = IoUtils.class.getName();

    public static final ProgressListener EMPTY_PROGRESS_LISTENER = ReflectUtils.newInstance(ProgressListener.class);
    
    public static final CancelCondition NO_CANCELATION = new CancelCondition() {

        @Override
        public boolean isCanceled() {
            return false;
        }
    };

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
        } finally {
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
        } catch (IOException e) {
            // hide exception, close source "silently"
            Log.e(LOG_TAG, "Error closing closeable source", e);
        }
    }

    /**
     * Copy bytes from an {@code InputStream} to an {@code OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@code BufferedInputStream}. Method doesn't close
     * streams after copying.
     * </p>
     * 
     * @param input the {@code InputStream} to read from, if {@code null} {@link IOException} will be thrown.
     * @param output the {@code OutputStream} to write to, if {@code null} {@link IOException} will be thrown.
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs or if the input or output is {@code null}
     * @throws ArithmeticException if the byte count is too large
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, EMPTY_PROGRESS_LISTENER);
    }

    /**
     * Copy bytes from an {@code InputStream} to an {@code OutputStream} with listening progress of copying.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@code BufferedInputStream}. Method doesn't close
     * streams after copying.
     * </p>
     * 
     * @param input the {@code InputStream} to read from, if {@code null} {@link IOException} will be thrown.
     * @param output the {@code OutputStream} to write to, if {@code null} {@link IOException} will be thrown.
     * @param listener listener of copying progress, must be not {@code null}.
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs or if the input or output is {@code null}
     * @throws ArithmeticException if the byte count is too large
     */
    public static long copy(InputStream input, OutputStream output, ProgressListener listener) throws IOException {
        return copy(input, output, listener, NO_CANCELATION);
    }

    /**
     * Copy bytes from an {@code InputStream} to an {@code OutputStream} with listening progress of copying.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@code BufferedInputStream}. Method doesn't close
     * streams after copying.
     * </p>
     * 
     * @param input the {@code InputStream} to read from, if {@code null} {@link IOException} will be thrown.
     * @param output the {@code OutputStream} to write to, if {@code null} {@link IOException} will be thrown.
     * @param listener listener of copying progress, must be not {@code null}.
     * @param canceler an controller to be used for canceling copying, must be not {@code null}.
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs or if the input or output is {@code null}
     * @throws ArithmeticException if the byte count is too large
     */
    public static long copy(InputStream input, OutputStream output, ProgressListener listener, CancelCondition canceler)
        throws IOException {
        Preconditions.checkNotNull(listener, "Progress listener must be not null!");
        Preconditions.checkNotNull(canceler, "Canceler must be not null!");

        if (input == null) {
            throw new IOException("Input stream is null!");
        }
        if (output == null) {
            throw new IOException("Output stream is null!");
        }

        InputStream bufferedInputStream = new BufferedInputStream(input);
        OutputStream bufferedOutputStream = new BufferedOutputStream(output);

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        listener.onProgress(0);
        while (-1 != (n = bufferedInputStream.read(buffer))) {
            bufferedOutputStream.write(buffer, 0, n);
            count += n;
            if (canceler.isCanceled()) {
                throw new CancelException("Copying is canceled!");
            }
            listener.onProgress(count);
        }
        bufferedOutputStream.flush();
        return count;
    }

    /**
     * Saves data to file and creates parent directories if needed.
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
            Files.createDirectory(targetFile.getParentFile());
            outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            outputStream.write(data);
        } finally {
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
            byte[] data = new byte[(int) sourceFile.length()];
            inputStream.read(data);
            return data;
        } finally {
            closeSilently(inputStream);
        }
    }

    public interface ProgressListener {

        public void onProgress(long bytesCopied);
    }

    public abstract static class PeriodProgressListener implements ProgressListener {

        private long publishEveryBytes;

        private long totalBytes;

        private long publishCount = -1;

        public PeriodProgressListener(long publishEveryBytes, long totalBytes) {
            this.publishEveryBytes = publishEveryBytes;
            this.totalBytes = totalBytes;
        }

        @Override
        public void onProgress(long bytesCopied) {
            long progress = bytesCopied / publishEveryBytes;
            if (progress > publishCount || bytesCopied == totalBytes) {
                publishCount = progress;
                onPeriodProgress(bytesCopied);
            }
        }

        public abstract void onPeriodProgress(long bytesCopied);

    }

    public interface CancelCondition {

        boolean isCanceled();

    }

}
