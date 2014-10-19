package com.danikula.android.garden.utils;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static void sleepOnMs(long timeMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeMs);
        }
        catch (InterruptedException e) {
            // do nothing
        }
    }
}
