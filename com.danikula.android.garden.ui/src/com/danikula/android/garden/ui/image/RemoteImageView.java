package com.danikula.android.garden.ui.image;

import com.danikula.android.garden.ui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RemoteImageView extends ImageView {

    private Drawable loadingImage;

    private Drawable errorImage;

    private String currentlyLoadedUrl;

    public RemoteImageView(Context context) {
        this(context, null);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.remoteImageViewStyle);
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RemoteImageView, defStyle,
                R.style.Widget_RemoteImageView);
        loadingImage = attributes.getDrawable(R.styleable.RemoteImageView_riLoadingImage);
        errorImage = attributes.getDrawable(R.styleable.RemoteImageView_riErrorImage);

        attributes.recycle();
    }

    protected void showLoadingStub() {

    }

    protected void showErrorStub() {

    }

    protected void showRealImage() {

    }

    public void loadImage(String url, ImageLoader imageLoader) {

        if (TextUtils.isEmpty(url)) {
            showErrorStub();
            setImageDrawable(errorImage);
        }
        else {
            showLoadingStub();
            setImageDrawable(loadingImage);
            currentlyLoadedUrl = url;
            imageLoader.loadImageAsynk(url, new LoadRemoteImageCallback());
        }
    }

    private final class LoadRemoteImageCallback implements LoadImageCallback {

        @Override
        public void onLoaded(String url, Bitmap bitmap) {
            if (url.equals(currentlyLoadedUrl)) {
                showRealImage();
                setImageBitmap(bitmap);
            }
        }

        @Override
        public void onError() {
            showErrorStub();
            setImageDrawable(errorImage);
        }
    }

}
