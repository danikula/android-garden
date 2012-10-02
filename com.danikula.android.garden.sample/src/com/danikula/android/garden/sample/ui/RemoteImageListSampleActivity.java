package com.danikula.android.garden.sample.ui;

import java.io.File;
import java.util.List;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.cache.BitmapFileBasedCache;
import com.danikula.android.garden.cache.Cache;
import com.danikula.android.garden.cache.LimitedMemeryAndDiscBitmapCache;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.ui.image.ImageLoader;
import com.danikula.android.garden.ui.image.RemoteImageView;
import com.danikula.android.garden.ui.list.MutableListAdapter;
import com.danikula.android.garden.ui.list.ViewHolder;
import com.danikula.android.garden.utils.AndroidUtils;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.widget.ListView;

public class RemoteImageListSampleActivity extends Activity {

    private static final int IMAGES_COUNT = 10;

    private static final String IMAGE_URL_PATTERN = "https://dl.dropbox.com/u/15506779/testimages/%s.jpg";

    @ViewById(android.R.id.list)
    private ListView listView;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File storageDir = new File(AndroidUtils.getApplicationExternalStorageDirectory(this), "image");
        Cache<String, Bitmap> imageCacheStorage = new LimitedMemeryAndDiscBitmapCache(storageDir, 2 * 1024 * 1024);
        imageLoader = new ImageLoader(imageCacheStorage);

        Aibolit.setInjectedContentView(this, R.layout.simple_list);

        List<String> imagesUrls = Lists.newArrayList();
        for (int i = 0; i < IMAGES_COUNT; i++) {
            imagesUrls.add(String.format(IMAGE_URL_PATTERN, i));
        }
        List<String> bigList = Lists.newArrayList();
        bigList.addAll(imagesUrls);
        bigList.addAll(imagesUrls);
        bigList.addAll(imagesUrls);
        bigList.addAll(imagesUrls);
        bigList.addAll(imagesUrls);

        ImagesAdapter adapter = new ImagesAdapter(this);
        adapter.setObjects(bigList);
        listView.setAdapter(adapter);
    }

    private final class ImagesAdapter extends MutableListAdapter<String> {

        public ImagesAdapter(Context context) {
            super(context, R.layout.image_list_item, new int[] { R.id.image });
        }

        @Override
        protected void bind(ViewHolder viewHolder, String url, int position) {
            RemoteImageView imageView = viewHolder.findView(R.id.image);
            imageView.loadImage(url, imageLoader);
        }

    }
}
