package com.danikula.android.garden.ui.validation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

public class InputValidator {

    private List<Validation> validations;

    public InputValidator() {
        this.validations = new LinkedList<Validation>();
    }

    public void addValidator(Validation validation) {
        validations.add(validation);
    }

    /**
     * Валидирует поля согласно ранее добавленным правилам. Данный метод применим только если компоненты имеют различные
     * идентификаторы.
     * 
     * @return Map&lt;Integer, String&gt; сообщения о невалидных полях, в качестве ключей сообщений выступают идентификторы
     *         компонентов.
     */
    public Map<Integer, String> validateWithKeys() {
        Map<Integer, String> validationMessages = new LinkedHashMap<Integer, String>();
        for (Validation validation : validations) {
            if (!validation.isValid()) {
                validationMessages.put(validation.getInputId(), validation.getMessage());
            }
        }
        return validationMessages;
    }
    
    public List<String> validate() {
        List<String> validationMessages = new LinkedList<String>();
        for (Validation validation : validations) {
            if (!validation.isValid()) {
                validationMessages.add(validation.getMessage());
            }
        }
        return validationMessages;
    }

    public String validateWithCommonResult() {
        return TextUtils.join("\n", validate());
    }

    public static class Builder {

        private Context context;

        private InputValidator inputValidator;

        public Builder(Context context) {
            this.context = context;
            this.inputValidator = new InputValidator();
        }

        public Builder notEmpty(EditText input, int messageId) {
            Validation validation = new Validation(input, new NotEmptyStringValidator(), getString(messageId));
            inputValidator.addValidator(validation);
            return this;
        }

        public Builder range(EditText input, int minValue, int maxValue, int messageId) {
            Validation validation = new Validation(input, new RangeIntegerValidator(minValue, maxValue), getString(messageId));
            inputValidator.addValidator(validation);
            return this;
        }

        public Builder maxLenght(EditText input, int length, int messageId) {
            Validation validation = new Validation(input, new MaxLenghtValidator(length), getString(messageId));
            inputValidator.addValidator(validation);
            return this;
        }

        public Builder allDigits(EditText input, int messageId) {
            inputValidator.addValidator(new Validation(input, new AllDigitsValidator(), getString(messageId)));
            return this;
        }

        public InputValidator build() {
            return inputValidator;
        }

        private String getString(int stringId) {
            return context.getString(stringId).toString();
        }

    }

}
