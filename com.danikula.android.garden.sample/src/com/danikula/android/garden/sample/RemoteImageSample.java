package com.danikula.android.garden.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectOnClickListener;
import com.danikula.aibolit.annotation.InjectView;

public class RemoteImageSample extends Activity {

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
