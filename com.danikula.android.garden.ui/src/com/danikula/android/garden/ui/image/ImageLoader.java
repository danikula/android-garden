package com.danikula.android.garden.ui.image;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.CacheException;
import com.danikula.android.garden.cache.EmptyCache;
import com.danikula.android.garden.io.FlushedInputStream;
import com.danikula.android.garden.io.IoUtils;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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
        checkNotNull(url, "Url must be not null!");
        checkNotNull(options, "Bitmap's options must be not null!");
        checkNotNull(callback, "Callback must be not null!");

        try {
            if (cacheStorage.contains(url)) {
                Bitmap bitmap = cacheStorage.get(url);
                callback.onLoaded(url, bitmap);
            }
            else {
                executor.execute(new LoadImageRunnable(url, options, callback));
            }
        }
        catch (CacheException e) {
            Log.e(LOG_TAG, "Error restoring image", e);
            callback.onError(url);
        }
    }

    private void loadAndCache(String url, Options options, LoadImageCallback loadImageCallback) {
        try {
            Bitmap bitmap = load(url, options);
            checkBitmapNotNull(bitmap, "Error loading image from url " + url);
            cache(url, bitmap);
            handler.post(new SuccessResultRunnable(loadImageCallback, bitmap, url));
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error loading image from " + url, e);
            handler.post(new FailResultRunnable(url, loadImageCallback));
        }
    }

    private Bitmap load(String url, Options options) throws IOException {
        InputStream inputStream = null;
        try {
            GenericUrl genericUrl = new GenericUrl(url);
            HttpRequest request = requestFactory.buildGetRequest(genericUrl);
            HttpResponse response = request.execute();
            if (!response.isSuccessStatusCode()) {
                String errorPattern = "Error invoking request: %s %s";
                throw new IOException(String.format(errorPattern, response.getStatusCode(), response.getStatusMessage()));
            }
            InputStream responseInputStream = response.getContent();
            inputStream = new FlushedInputStream(responseInputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap == null) {
                throw new IOException("Error decoding image from " + url);
            }
            return bitmap;
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
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

        private LoadImageRunnable(String url, Options options, LoadImageCallback loadImageCallback) {
            this.url = url;
            this.loadImageCallback = loadImageCallback;
            this.options = options;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            loadAndCache(url, options, loadImageCallback);
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
