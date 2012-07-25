package com.danikula.android.garden.ui.view;

import com.danikula.android.garden.ui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * {@link TextView} with capability to load custom fonts.
 * 
 * @author Alexey Danilov
 */
public class TypefacedTextView extends TextView {

    public TypefacedTextView(Context context) {
        this(context, null);
    }

    public TypefacedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.typefacedTextViewStyle);
    }

    public TypefacedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView, defStyle, 0);
        String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_customTypeface);
        int styleIndex = styledAttrs.getInt(R.styleable.TypefacedTextView_textStyle, 0);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            setTypeface(typeface, styleIndex); 
        }
    }
}