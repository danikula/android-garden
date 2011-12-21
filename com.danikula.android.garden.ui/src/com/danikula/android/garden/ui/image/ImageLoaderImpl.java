package com.danikula.android.garden.ui.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.danikula.android.garden.io.FlushedInputStream;
import com.danikula.android.garden.io.IoUtils;
import com.danikula.android.garden.storage.EmptyStorage;
import com.danikula.android.garden.storage.Storage;
import com.danikula.android.garden.utils.Validate;

public class ImageLoaderImpl implements ImageLoader {
    
    private static final String LOG_TAG = ImageLoaderImpl.class.getName();
    
    private Storage<String, Bitmap> cacheStorage;
    
    public ImageLoaderImpl() {
        this(new EmptyStorage<String, Bitmap>());
    }

    public ImageLoaderImpl(Storage<String, Bitmap> cacheStorage) {
        Validate.notNull(cacheStorage, "Cache storage can not be null!");
        this.cacheStorage = cacheStorage;
    }
    
    @Override
    public void loadImageAsynk(String url, LoadImageCallback callback) {
        Validate.notNull(url, "URL can not be null!");
        Validate.notNull(callback, "LoadImageCallback can not be null!");
        
        if(cacheStorage.contains(url)) {
            callback.onLoaded(url, cacheStorage.get(url));
        } else {
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
        cacheStorage.put(url, bitmap);
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
            } else {
                loadImageCallback.onLoaded(url, bitmap);
            }
        }
    }

}
