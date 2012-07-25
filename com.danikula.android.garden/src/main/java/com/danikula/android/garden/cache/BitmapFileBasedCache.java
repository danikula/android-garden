package com.danikula.android.garden.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.danikula.android.garden.io.IoUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapFileBasedCache extends FileBasedCache<Bitmap> {

    private static final String NO_MEDIA_FILE_NAME = ".nomedia";

    private static final int DEFAULT_QUALITY = 100;

    private int quality;

    public BitmapFileBasedCache(String storagePath, boolean scannable, int quality) {
        super(storagePath);
        this.quality = quality;

        if (!scannable) {
            createNoMediaMarkerFile(storagePath);
        }
    }

    public BitmapFileBasedCache(String storagePath, boolean scannable) {
        this(storagePath, scannable, DEFAULT_QUALITY);
    }

    public BitmapFileBasedCache(String storagePath) {
        this(storagePath, false, DEFAULT_QUALITY);
    }

    private synchronized void createNoMediaMarkerFile(String storagePath) {
        try {
            // see details here http://stackoverflow.com/questions/2556065/stop-mediascanner-scanning-of-certain-directory
            new File(storagePath).mkdirs();
            new File(storagePath, NO_MEDIA_FILE_NAME).createNewFile();
        }
        catch (IOException e) {
            // can't create no media marker, ignore it
        }
    }

    @Override
    protected void write(File file, Bitmap bitmap) throws IOException {
        CompressFormat compressFormat = bitmap.hasAlpha() ? CompressFormat.PNG : CompressFormat.JPEG;
        File bitmapFile = new File(file.getAbsolutePath() + "." + compressFormat.toString());
        File tempFile = new File(bitmapFile.getAbsoluteFile() + ".temp");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(tempFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bitmap.compress(compressFormat, quality, bufferedOutputStream);
            bufferedOutputStream.flush();
            tempFile.renameTo(bitmapFile);
        }
        finally {
            IoUtils.closeSilently(outputStream);
        }
    }

    @Override
    protected Bitmap read(File file) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap == null) {
            file.delete();
            throw new IOException("Error decoding image " + file.getAbsolutePath());
        }
        return bitmap;
    }
}
