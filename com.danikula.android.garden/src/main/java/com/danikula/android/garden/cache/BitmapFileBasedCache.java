package com.danikula.android.garden.cache;

import static com.google.common.base.Preconditions.checkArgument;

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

    private int quality = DEFAULT_QUALITY;

    public BitmapFileBasedCache(File storageDir) {
        super(storageDir, "image");
        createNoMediaMarkerFile(storageDir);
    }

    private void setBitmapQuality(int quality) {
        checkArgument(quality > 0 && quality <= 100, "Quality must be > 0 and <=100!");
        this.quality = quality;
    }

    private void createNoMediaMarkerFile(File storageDir) {
        try {
            // see details here http://stackoverflow.com/questions/2556065/stop-mediascanner-scanning-of-certain-directory
            storageDir.mkdirs();
            new File(storageDir, NO_MEDIA_FILE_NAME).createNewFile();
        }
        catch (IOException e) {
            // can't create no media marker, ignore it
        }
    }

    @Override
    protected void write(File bitmapFile, Bitmap bitmap) throws IOException {
        CompressFormat compressFormat = bitmap.hasAlpha() ? CompressFormat.PNG : CompressFormat.JPEG;
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
