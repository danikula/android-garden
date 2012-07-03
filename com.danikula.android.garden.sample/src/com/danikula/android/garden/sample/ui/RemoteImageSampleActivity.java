package com.danikula.android.garden.sample.ui;

import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.OnClick;
import com.danikula.aibolit.annotation.ViewById;
import com.danikula.android.garden.sample.R;
import com.danikula.android.garden.ui.image.ImageLoader;
import com.danikula.android.garden.ui.image.ImageLoaderImpl;
import com.danikula.android.garden.ui.image.RemoteImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class RemoteImageSampleActivity extends Activity {

    @ViewById(R.id.remoteImageView)
    private RemoteImageView remoteImageView;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoaderImpl();

        Aibolit.setInjectedContentView(this, R.layout.remote_image_sample);
    }

    @OnClick(R.id.loadButton)
    private void onLoadButtonClick(View view) {
        remoteImageView.loadImage("http://www.android.com/images/robot-sdk.png", imageLoader);
    }
}
