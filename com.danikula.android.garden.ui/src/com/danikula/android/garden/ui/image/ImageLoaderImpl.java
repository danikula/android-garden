package com.danikula.android.garden.ui.image;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.danikula.android.garden.cache.EmptyCache;
import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.CacheException;
import com.danikula.android.garden.io.FlushedInputStream;
import com.danikula.android.garden.io.IoUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageLoaderImpl implements ImageLoader {

    private static final String LOG_TAG = ImageLoaderImpl.class.getName();

    private Cache<String, Bitmap> cacheStorage;

    public ImageLoaderImpl() {
        this(new EmptyCache<String, Bitmap>());
    }

    public ImageLoaderImpl(Cache<String, Bitmap> cacheStorage) {
        this.cacheStorage = checkNotNull(cacheStorage, "Cache storage must be not null!");
    }

    @Override
    public void loadImageAsynk(String url, LoadImageCallback callback) {
        checkNotNull(url, "Url must be not null!");
        checkNotNull(callback, "Callback must be not null!");

        if (cacheStorage.contains(url)) {
            callback.onLoaded(url, cacheStorage.get(url));
        }
        else {
            new DownloadImageTask(url, callback).execute();
        }
    }

    private Bitmap load(URL url) throws IOException {
        InputStream inputStream = null;
        try {
            URLConnection urlConnection = url.openConnection();
            inputStream = new FlushedInputStream(urlConnection.getInputStream());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                throw new IOException("Error decoding image from " + url);
            }
            return bitmap;
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error loading image from " + url);
            throw e;
        }
        finally {
            IoUtils.closeSilently(inputStream);
        }
    }

    private synchronized void cache(String url, Bitmap bitmap) {
        try {
            cacheStorage.put(url, bitmap);
        } catch (CacheException e) {
            // just log & ignore this error
            Log.e(LOG_TAG, "Error saving bitmap in cache", e);
        }
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;

        private LoadImageCallback loadImageCallback;

        private DownloadImageTask(String url, LoadImageCallback loadImageCallback) {
            this.loadImageCallback = loadImageCallback;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Bitmap bitmap = load(new URL(url));
                cache(url, bitmap);
                return bitmap;
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error loading image from " + url, e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                loadImageCallback.onError();
            }
            else {
                loadImageCallback.onLoaded(url, bitmap);
            }
        }
    }

}
