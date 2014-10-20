package com.danikula.android.garden.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import junit.framework.TestCase;
import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


public class ReflectUtilsTest  {

    @Test
    public void testGetStringConstantValues() {
        String[] stringFieldValues = ReflectUtils.getStringConstantValues(TestClass.class);
        Set<String> stringFieldValuesSet = new HashSet<String>(Arrays.asList(stringFieldValues));
        Set<String> expectedResultSet = new HashSet<String>(Arrays.asList("one", "four", "zero"));
        assertTrue(stringFieldValuesSet.equals(expectedResultSet));
    }
    
    private static class BaseTestClass {
        
        private static final String CONST_ZERO = "zero";
    }

    private static final class TestClass extends BaseTestClass {

        private static final String CONST = "one";

        private int intField = 1;

        private final String finalField = "two";

        private static String staticField = "three";
        
        private static final String CONST_FOUR = "four";


    }

}
