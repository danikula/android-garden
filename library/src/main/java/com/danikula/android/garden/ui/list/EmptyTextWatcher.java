package com.danikula.android.garden.ui.list;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Empty implementation on {@link TextWatcher}
 * 
 * @author Alexey Danilov
 */
public class EmptyTextWatcher implements TextWatcher {

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}
