package com.danikula.android.garden.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import android.os.Bundle;

/**
 * Contains useful utility methods to simplify work with {@link Bundle}.
 * 
 * @author Alexey Danilov <danikula@gmail.com>
 */
public class Bundles {

    public static Bundle newLongValueBundle(String argName, long longValue) {
        checkNotNull(argName, "Argument's name must be not null!");

        Bundle args = new Bundle();
        args.putLong(argName, longValue);
        return args;
    }

    public static Bundle newIntValueBundle(String argName, int intValue) {
        checkNotNull(argName, "Argument's name must be not null!");

        Bundle args = new Bundle();
        args.putInt(argName, intValue);
        return args;
    }

    public static Bundle newStringValueBundle(String argName, String stringValue) {
        checkNotNull(argName, "Argument's name must be not null!");

        Bundle args = new Bundle();
        args.putString(argName, stringValue);
        return args;
    }

    public static Bundle newBooleanValueBundle(String argName, boolean value) {
        checkNotNull(argName, "Argument's name must be not null!");
        
        Bundle args = new Bundle();
        args.putBoolean(argName, value);
        return args;
    }

    public static long getRequiredLongValue(Bundle bundle, String argName) {
        checkContains(bundle, argName);

        return bundle.getLong(argName);
    }

    public static int getRequiredIntValue(Bundle bundle, String argName) {
        checkContains(bundle, argName);

        return bundle.getInt(argName);
    }

    public static String getRequiredStringValue(Bundle bundle, String argName) {
        checkContains(bundle, argName);

        return bundle.getString(argName);
    }

    public static boolean getRequiredBooleanValue(Bundle bundle, String argName) {
        checkContains(bundle, argName);

        return bundle.getBoolean(argName);
    }

    public static void checkContains(Bundle bundle, String... argNames) {
        checkNotNull(bundle, "Bundle must be not null!");
        for (String argName : argNames) {
            checkArgument(bundle.containsKey(argName), "Bundle doesn't contain required argument '%s'", argName);
        }
    }
}
