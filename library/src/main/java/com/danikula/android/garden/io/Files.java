package com.danikula.android.garden.io;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains methods for manipulating files.
 *
 * @author Alexey Danilov (danikula@gmail.com)
 */
public class Files {

    /**
     * Creates a directory with specified path.
     * <p/>
     * Directory can exist, but in this case should be directory, not file.
     *
     * @param directory directory to be created, must be not {@code null} and must be not file if exists.
     * @throws IOException if file is exist and it is not a directory of if any error occurs.
     */
    public static void createDirectory(File directory) throws IOException {
        Preconditions.checkNotNull(directory, "File must be not null!");

        if (directory.exists()) {
            checkArgument(directory.isDirectory(), "File is not directory!");
        } else {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                String error = String.format("Directory %s can't be created", directory.getAbsolutePath());
                throw new IOException(error);
            }
        }
    }

    /**
     * Delete file or directory.
     *
     * @param file a file to be deleted, must be not {@code null}.
     * @throws IOException if directory can not be deleted.
     */
    public static void delete(File file) throws IOException {
        checkNotNull(file, "File must be not null!");

        if (file.isFile() && file.exists()) {
            deleteOrThrow(file);
        } else {
            cleanDirectory(file);
            deleteOrThrow(file);
        }
    }

    /**
     * Deletes all files in directory.
     *
     * @param file a directory to be cleaned.
     * @throws IOException if directory can not be cleaned.
     */
    public static void cleanDirectory(File file) throws IOException {
        checkNotNull(file, "File must be not null!");

        if (!file.exists()) {
            return;
        }
        checkArgument(file.isDirectory(), "File must be directory!");
        File[] contentFiles = file.listFiles();
        if (contentFiles != null) {
            for (File contentFile : contentFiles) {
                delete(contentFile);
            }
        }
    }

    public static void deleteSafe(File file) {
        try {
            if (file != null) {
                delete(file);
            }
        } catch (IOException e) {
            Log.e("Files", "Error deleting file " + file, e);
        }
    }

    public static void createEmptyFile(String path, long length) throws IOException {
        OutputStream out = null;
        byte[] buffer = new byte[8 * 1024];
        long wrote = 0;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(path)));
            while (wrote < length) {
                int count = (int) Math.min(buffer.length, length - wrote);
                out.write(buffer, 0, count);
                wrote += buffer.length;
            }
        } finally {
            IoUtils.closeSilently(out);
        }
    }

    public static boolean isFileExist(final String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    private static void deleteOrThrow(File file) throws IOException {
        checkNotNull(file, "File must be not null!");

        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new IOException(String.format("File %s can't be deleted", file.getAbsolutePath()));
            }
        }
    }



}
