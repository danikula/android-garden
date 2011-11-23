package com.danikula.android.garden.ui.validation;

public class RangeIntegerValidator implements Validator {

    private int minValue;

    private int maxValue;

    public RangeIntegerValidator(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean validate(String value) {
        boolean isValid = false;
        try {
            int intValue = Integer.parseInt(value);
            isValid = intValue >= minValue && intValue <= maxValue;
        }
        catch (NumberFormatException e) {
            // do nothing, by default value is not valid
        }
        return isValid;
    }
}