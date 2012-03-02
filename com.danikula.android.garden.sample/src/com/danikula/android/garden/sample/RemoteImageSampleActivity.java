package com.danikula.android.garden.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectOnClickListener;
import com.danikula.aibolit.annotation.InjectView;
import com.danikula.android.garden.ui.image.ImageLoader;
import com.danikula.android.garden.ui.image.ImageLoaderImpl;
import com.danikula.android.garden.ui.image.RemoteImageView;

public class RemoteImageSampleActivity extends Activity {

    @InjectView(R.id.remoteImageView)
    private RemoteImageView remoteImageView;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoaderImpl();

        Aibolit.setInjectedContentView(this, R.layout.remote_image_sample);
    }

    @InjectOnClickListener(R.id.loadButton)
    private void onLoadButtonClick(View view) {
        remoteImageView.loadImage("http://www.android.com/images/robot-sdk.png", imageLoader);
    }
}
