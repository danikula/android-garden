package com.danikula.android.garden.ui.image;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.CacheException;
import com.danikula.android.garden.cache.EmptyCache;
import com.danikula.android.garden.io.FlushedInputStream;
import com.danikula.android.garden.io.IoUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Optional;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

public class ImageLoader {

    private static final String LOG_TAG = ImageLoader.class.getName();

    private Cache<String, Bitmap> cacheStorage;

    private HttpTransport httpTransport;

    private HttpRequestFactory requestFactory;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private Handler handler = new Handler();

    public ImageLoader() {
        this(new EmptyCache<String, Bitmap>());
    }

    public ImageLoader(Cache<String, Bitmap> cacheStorage) {
        this.cacheStorage = checkNotNull(cacheStorage, "Cache storage must be not null!");
        httpTransport = AndroidHttp.newCompatibleTransport();
        requestFactory = httpTransport.createRequestFactory();
    }

    public void clearCache() {
        cacheStorage.clear();
    }

    public void setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        checkNotNull(httpRequestInitializer, "Initializer must be not null!");
        requestFactory = httpTransport.createRequestFactory(httpRequestInitializer);
    }

    public void loadImageAsynk(String url, BitmapFactory.Options options, LoadImageCallback callback) {
        loadImageAsynk(url, options, Optional.<Rect> absent(), callback);
    }

    public void loadImageAsynk(String url, BitmapFactory.Options options, Optional<Rect> requiredSize, LoadImageCallback callback) {
        checkNotNull(url, "Url must be not null!");
        checkNotNull(options, "Bitmap's options must be not null!");
        checkNotNull(callback, "Callback must be not null!");

        try {
            if (cacheStorage.contains(url)) {
                Bitmap bitmap = cacheStorage.get(url);
                callback.onLoaded(url, bitmap);
            }
            else {
                executor.execute(new LoadImageRunnable(url, options, requiredSize, callback));
            }
        }
        catch (CacheException e) {
            Log.e(LOG_TAG, "Error restoring image", e);

            executor.execute(new LoadImageRunnable(url, options, requiredSize, callback));
        }
    }

    private void loadAndCache(String url, Options options, Optional<Rect> requiredSize, LoadImageCallback loadImageCallback) {
        try {
            Bitmap bitmap = load(url, options, requiredSize);
            checkBitmapNotNull(bitmap, "Error loading image from url " + url);
            cache(url, bitmap);
            handler.post(new SuccessResultRunnable(loadImageCallback, bitmap, url));
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error loading image from " + url, e);
            handler.post(new FailResultRunnable(url, loadImageCallback));
        }
    }

    private Bitmap load(String url, Options options, Optional<Rect> optionalRequiredSize) throws IOException {
        if (optionalRequiredSize.isPresent()) {
            Options sourceOptions = readBitmapOptions(url);
            Rect requiredSize = optionalRequiredSize.get();
            int inSampleSize = calculateInSampleSize(sourceOptions, requiredSize.right, requiredSize.bottom);
            options.inSampleSize = inSampleSize;

            String debugMessageFormat = "Use scaling factor %d to scale original image with size %d*%d";
            Log.d(LOG_TAG, String.format(debugMessageFormat, inSampleSize, sourceOptions.outWidth, sourceOptions.outHeight));
        }

        Bitmap bitmap = decode(url, options);

        if (optionalRequiredSize.isPresent() && bitmap != null) {
            int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
            Log.d(LOG_TAG, String.format("Image loaded: %s*%s, size: %d", bitmap.getWidth(), bitmap.getHeight(), bitmapSize));
        }
        return bitmap;
    }

    private BitmapFactory.Options readBitmapOptions(String url) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(url);
            BitmapFactory.Options outOptions = new BitmapFactory.Options();
            outOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, outOptions);
            return outOptions;
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            }
            else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private Bitmap decode(String url, Options options) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(url);
            return BitmapFactory.decodeStream(inputStream, null, options);
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
    }

    private InputStream getInputStream(String url) throws IOException {
        GenericUrl genericUrl = new GenericUrl(url);
        HttpRequest request = requestFactory.buildGetRequest(genericUrl);
        HttpResponse response = request.execute();
        if (!response.isSuccessStatusCode()) {
            String errorPattern = "Error invoking request: %s %s";
            throw new IOException(String.format(errorPattern, response.getStatusCode(), response.getStatusMessage()));
        }
        InputStream responseInputStream = response.getContent();
        return new FlushedInputStream(responseInputStream);
    }

    private void checkBitmapNotNull(Bitmap bitmap, String error) throws CacheException {
        if (bitmap == null) {
            throw new CacheException(error);
        }
    }

    private void cache(String url, Bitmap bitmap) {
        try {
            cacheStorage.put(url, bitmap);
        }
        catch (CacheException e) {
            // just log & ignore this error
            Log.e(LOG_TAG, "Error saving bitmap in cache", e);
        }
    }

    private class LoadImageRunnable implements Runnable {

        private String url;

        private LoadImageCallback loadImageCallback;

        private BitmapFactory.Options options;

        private Optional<Rect> maxSize;

        private LoadImageRunnable(String url, Options options, Optional<Rect> maxSize, LoadImageCallback loadImageCallback) {
            this.url = url;
            this.loadImageCallback = loadImageCallback;
            this.options = options;
            this.maxSize = maxSize;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            loadAndCache(url, options, maxSize, loadImageCallback);
        }
    }

    private static final class SuccessResultRunnable implements Runnable {

        private LoadImageCallback loadImageCallback;

        private Bitmap bitmap;

        private String url;

        private SuccessResultRunnable(LoadImageCallback loadImageCallback, Bitmap bitmap, String url) {
            this.loadImageCallback = loadImageCallback;
            this.bitmap = bitmap;
            this.url = url;
        }

        @Override
        public void run() {
            loadImageCallback.onLoaded(url, bitmap);
        }
    }

    private static final class FailResultRunnable implements Runnable {

        private String url;

        private LoadImageCallback loadImageCallback;

        private FailResultRunnable(String url, LoadImageCallback loadImageCallback) {
            this.url = url;
            this.loadImageCallback = loadImageCallback;
        }

        @Override
        public void run() {
            loadImageCallback.onError(url);
        }
    }
}
