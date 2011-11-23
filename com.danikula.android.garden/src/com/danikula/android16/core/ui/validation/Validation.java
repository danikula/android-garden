package com.danikula.android16.core.ui.validation;

import android.widget.EditText;

public class Validation {

    private EditText input;

    private Validator validator;

    private String message;

    public Validation(EditText input, Validator validator, String message) {
        this.input = input;
        this.validator = validator;
        this.message = message;
    }

    public int getInputId() {
        return input.getId();
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return validator.validate(input.getText().toString());
    }
}