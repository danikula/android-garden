package com.danikula.android.garden.utils;

import android.os.Bundle;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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

    public static Bundle newBundle(String argName0, int intValue0, String argName1, int intValue1) {
        checkNotNull(argName0, "Argument's name must be not null!");
        checkNotNull(argName1, "Argument's name must be not null!");

        Bundle bundle = newIntValueBundle(argName0, intValue0);
        bundle.putInt(argName1, intValue1);
        return bundle;
    }

    public static Bundle newBundle(String argName0, String stringValue0, String argName1, String stringValue1) {
        checkNotNull(argName0, "Argument's name must be not null!");
        checkNotNull(argName1, "Argument's name must be not null!");

        Bundle bundle = newStringValueBundle(argName0, stringValue0);
        bundle.putString(argName1, stringValue1);
        return bundle;
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

    public static <T> T getRequiredSerializableValue(Bundle bundle, String argName) {
        checkContains(bundle, argName);

        return (T) bundle.getSerializable(argName);
    }

    public static void checkContains(Bundle bundle, String... argNames) {
        checkNotNull(bundle, "Bundle must be not null!");
        for (String argName : argNames) {
            checkArgument(bundle.containsKey(argName), "Bundle doesn't contain required argument '%s'", argName);
        }
    }

    public static String dump(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder result = new StringBuilder("");
        for (String key : bundle.keySet()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            Object value = bundle.get(key);
            String valueType = value == null ? "unknown type" : value.getClass().getName();
            value = Bundle.class.getName().equals(valueType) ? dump((Bundle) value) : value;
            result.append(String.format("%s=%s (%s)", key, value, valueType));
        }
        result.insert(0, "Bundle[").append("]");
        return result.toString();
    }
}
