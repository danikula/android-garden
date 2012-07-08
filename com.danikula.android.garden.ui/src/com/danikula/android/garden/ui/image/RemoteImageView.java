package com.danikula.android.garden.ui.image;

import com.danikula.android.garden.ui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class RemoteImageView extends ImageView {

    private Drawable loadingImage;

    private Drawable errorImage;

    private String currentlyLoadedUrl;

    private static BitmapFactory.Options bitmapOptions;

    public RemoteImageView(Context context) {
        this(context, null);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.remoteImageViewStyle);
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        bitmapOptions = createDefaultBitmapOptions();
        
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RemoteImageView, defStyle,
                R.style.Widget_RemoteImageView);
        
        loadingImage = attributes.getDrawable(R.styleable.RemoteImageView_loadingImage);
        errorImage = attributes.getDrawable(R.styleable.RemoteImageView_errorImage);
        
        int inDensity = attributes.getInt(R.styleable.RemoteImageView_density, -1);
        if (inDensity != -1) {
            setInDensity(inDensity);
        }

        attributes.recycle();
    }

    private BitmapFactory.Options createDefaultBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inScaled = true;
        options.inDensity = DisplayMetrics.DENSITY_MEDIUM;
        options.inTargetDensity = getContext().getResources().getDisplayMetrics().densityDpi;
        return options;
    }

    protected void showLoadingStub() {

    }

    protected void showErrorStub() {

    }

    protected void showRealImage() {

    }
    
    public void setInDensity(int inDensity) {
        bitmapOptions.inDensity = inDensity;
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
            imageLoader.loadImageAsynk(url, bitmapOptions, new LoadRemoteImageCallback());
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
        public void onError(String url) {
            if (url.equals(currentlyLoadedUrl)) {
                showErrorStub();
                setImageDrawable(errorImage);
            }
        }
    }

}
