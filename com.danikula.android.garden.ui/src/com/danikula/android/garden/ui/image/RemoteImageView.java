package com.danikula.android.garden.ui.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.danikula.android.garden.ui.R;
import com.danikula.android.garden.utils.Validate;

public class RemoteImageView extends ImageView {
    
    private Drawable loadingImage;

    private Drawable errorImage;

    public RemoteImageView(Context context) {
        this(context, null);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.remoteImageViewStyle);
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.obtainStyledAttributes(
            attrs, R.styleable.RemoteImageView, defStyle, R.style.Widget_RemoteImageView);
        
        loadingImage = attributes.getDrawable(R.styleable.RemoteImageView_loadingImage);
        errorImage = attributes.getDrawable(R.styleable.RemoteImageView_errorImage);

        attributes.recycle();
    }

    public void loadImage(String url, ImageLoader imageLoader) {
        Validate.notNull(imageLoader, "ImageLoader can not be null!");

        if (url == null) {
            setImageDrawable(errorImage);
        } else {
            setImageDrawable(loadingImage);
            imageLoader.loadImageAsynk(url, new LoadRemoteImageCallback());
        }
    }

    private final class LoadRemoteImageCallback implements LoadImageCallback {

        @Override
        public void onLoaded(Bitmap bitmap) {
            setImageBitmap(bitmap);
        }

        @Override
        public void onError() {
            setImageDrawable(errorImage);
        }
    }

}
