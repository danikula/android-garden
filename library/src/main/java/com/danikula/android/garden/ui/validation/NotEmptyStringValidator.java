package com.danikula.android.garden.ui.validation;

import android.text.TextUtils;

public class NotEmptyStringValidator implements Validator {

    @Override
    public boolean validate(String value) {
        return !TextUtils.isEmpty(value);
    }
}