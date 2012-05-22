package com.danikula.android.garden.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void sleep(long timeMs) {
        try {
            Thread.currentThread().sleep(timeMs);
        }
        catch (InterruptedException e) {
            // do nothing
        }
    }

    public static <I, O> List<O> convert(List<I> source, Converter<I, O> converter) {
        List<O> result = null;
        if (source != null) {
            result = new ArrayList<O>();
            for (I sourceItem : source) {
                O targetItem = converter.convert(sourceItem);
                result.add(targetItem);
            }
        }
        return result;
    }

}
