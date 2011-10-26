package com.danikula.androidkit.aibolit.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.ui.TestInjectActivity;

public class TestInputValidation extends ActivityInstrumentationTestCase2 {

    public TestInputValidation() {
        super("com.danikula.androidkit.aibolit", TestInjectActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testInjection() {
        TextView textView = ((TestInjectActivity)getActivity()).textView;
        
        assertNotNull(textView);
        
    }

}
