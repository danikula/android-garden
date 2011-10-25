package com.danikula.android16.core.storage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.danikula.android16.core.utils.IoUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class BitmapFileBasedStorage extends FileBasedStorage<Bitmap> {

    private static final int DEFAULT_QUALITY = 100;

    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;

    private CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;

    private int quality;

    public BitmapFileBasedStorage(String storagePath, CompressFormat compressFormat, int quality) {
        super(storagePath, compressFormat.toString().toLowerCase());
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    public BitmapFileBasedStorage(String storagePath) {
        this(storagePath, DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY);
    }

    @Override
    protected void write(File file, Bitmap bitmap) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bitmap.compress(compressFormat, quality, bufferedOutputStream);
        }
        finally {
            IoUtils.closeSilently(outputStream);
        }
    }

    @Override
    protected Bitmap read(File file) throws IOException {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

}
