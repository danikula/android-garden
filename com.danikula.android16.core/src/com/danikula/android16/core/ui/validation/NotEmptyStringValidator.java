package com.danikula.android16.core.ui.validation;

import android.text.TextUtils;

public class NotEmptyStringValidator implements Validator {

    @Override
    public boolean validate(String value) {
        return !TextUtils.isEmpty(value);
    }
}