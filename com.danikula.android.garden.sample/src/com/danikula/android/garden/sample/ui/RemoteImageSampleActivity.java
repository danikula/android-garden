package com.danikula.android.garden.sample.ui;

import java.io.File;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.cache.BitmapFileBasedCache;
import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.ui.image.ImageLoader;
import com.danikula.android.garden.ui.image.RemoteImageView;
import com.danikula.android.garden.utils.AndroidUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;

public class RemoteImageSampleActivity extends Activity {

    private static final String IMAGE_URL = "https://dl.dropbox.com/u/15506779/testimages/0.jpg";

    @ViewById(R.id.image)
    private RemoteImageView remoteImageView;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path = new File(AndroidUtils.getApplicationExternalStorageDirectory(this), "image").getAbsolutePath();
        Cache<String, Bitmap> imageCacheStorage = new BitmapFileBasedCache(path, false, CompressFormat.PNG, 100);
        imageLoader = new ImageLoader(imageCacheStorage);

        Aibolit.setInjectedContentView(this, R.layout.remote_image_activity);
    }

    @OnClick(R.id.load)
    private void onLoadButtonClick(View view) {
        remoteImageView.loadImage(IMAGE_URL, imageLoader);
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
//        remoteImageView.setImageBitmap(b);
    }
}