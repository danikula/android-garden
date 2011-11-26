package com.danikula.android.garden.ui.image;

import android.graphics.Bitmap;

public interface LoadImageCallback {

    void onLoaded(Bitmap bitmap);
    
    void onError();
    
}
