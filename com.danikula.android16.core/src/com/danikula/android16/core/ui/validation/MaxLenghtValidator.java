package com.danikula.android16.core.ui.validation;


public class MaxLenghtValidator implements Validator {
    
    private int maxLenght;
    
    public MaxLenghtValidator(int maxLenght) {
        this.maxLenght = maxLenght;
    }

    @Override
    public boolean validate(String value) {
        return value != null && value.length() <= maxLenght;
    }
}