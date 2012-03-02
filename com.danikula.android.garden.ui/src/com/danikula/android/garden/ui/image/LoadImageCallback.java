package com.danikula.android.garden.ui.image;

import android.graphics.Bitmap;

public interface LoadImageCallback {

    void onError();

    void onLoaded(String url, Bitmap bitmap);

}
