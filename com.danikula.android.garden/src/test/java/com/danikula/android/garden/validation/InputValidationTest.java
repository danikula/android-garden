//package com.danikula.android.garden.validation;
//
//import android.test.ActivityInstrumentationTestCase2;
//import android.widget.EditText;
//
//public class TestInputValidation {
//
//    private EditText nameEditText;
//
//    private EditText phoneEditText;
////
////    @Override
////    protected void setUp() throws Exception {
////        super.setUp();
////        nameEditText = (EditText) getActivity().findViewById(R.id.nameEditText);
////        phoneEditText = (EditText) getActivity().findViewById(R.id.phoneEditText);
////    }
////
////    public void testNotEmpty() {
////        InputValidator validator = new InputValidator.Builder(getActivity())
////                .notEmpty(nameEditText, R.string.profile_edit_empty_name)
////                .notEmpty(phoneEditText, R.string.profile_edit_empty_phone).build();
////        assertEquals(2, validator.validate().size());
////    }
////
////    public void testNotEmptyWitResult() {
////        InputValidator validator = new InputValidator.Builder(getActivity())
////                .notEmpty(nameEditText, R.string.profile_edit_empty_name)
////                .notEmpty(phoneEditText, R.string.profile_edit_empty_phone).build();
////        String errorMessage0 = getActivity().getString(R.string.profile_edit_empty_name);
////        String errorMessage1 = getActivity().getString(R.string.profile_edit_empty_phone);
////        assertEquals(String.format("%s\n%s", errorMessage0, errorMessage1), validator.validateWithCommonResult());
////    }
//
////    public void testRangeValid() {
//////        setValue(nameEditText, "9");
////        InputValidator validator = new InputValidator.Builder(getActivity()).range(nameEditText, 2, 10,
////                R.string.profile_edit_empty_name).build();
////        assertEquals(0, validator.validate().size());
////    }
//
////    public void testRangeNotValid() {
////        setValue(nameEditText, "123");
////        InputValidator validator = new InputValidator.Builder(getActivity()).range(nameEditText, 2, 7,
////                R.string.profile_edit_empty_name).build();
////
////        assertEquals(1, validator.validate().size());
////    }
////
////    public void testDigits() {
////        setValue(nameEditText, "12302435");
////        InputValidator validator = new InputValidator.Builder(getActivity()).allDigits(nameEditText,
////                R.string.profile_edit_empty_name).build();
////
////        assertEquals(0, validator.validate().size());
////    }
//
////    public void testRangeDigitsValid() {
////        getActivity().runOnUiThread(new Runnable() {
////
////            @Override
////            public void run() {
////                nameEditText.setText("123");
////             // @formatter:off
////                InputValidator validator = new InputValidator.Builder(getActivity())
////                        .range(nameEditText, 2, 200, R.string.profile_edit_empty_name)
////                        .allDigits(nameEditText, R.string.profile_edit_empty_name).build();
////                // @formatter:on
////                assertEquals(0, validator.validate().size());
////            }
////        });
////    }
////
////    private void setValue(final EditText editText, final String vale) {
////        getActivity().runOnUiThread(new Runnable() {
////
////            @Override
////            public void run() {
////                editText.setText(vale);
////            }
////        });
////    }
//
//}
