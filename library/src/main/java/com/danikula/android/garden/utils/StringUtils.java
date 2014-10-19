package com.danikula.android.garden.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class StringUtils {
    
    public static String computeMD5(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digestBytes = messageDigest.digest(string.getBytes());
            return bytesToHexString(digestBytes);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static boolean isAnyEmpty(CharSequence... strings) {
        for (CharSequence string : strings) {
            if (TextUtils.isEmpty(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areAllEmpty(CharSequence... strings) {
        for (CharSequence string : strings) {
            if (!TextUtils.isEmpty(string)) {
                return false;
            }
        }
        return true;
    }
    
    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
