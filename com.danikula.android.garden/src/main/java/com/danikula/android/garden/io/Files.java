package com.danikula.android.garden.io;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Preconditions;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Contains methods for manipulating files.
 * 
 * @author Alexey Danilov (danikula@gmail.com)
 */
public class Files {

    /**
     * Creates a directory with specified path.
     * 
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
