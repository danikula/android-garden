package com.danikula.android.garden.ui.validation;

import android.text.TextUtils;


public class AllDigitsValidator implements Validator {
    
    @Override
    public boolean validate(String value) {
        return TextUtils.isDigitsOnly(value);
    }
}