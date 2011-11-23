package com.danikula.android16.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TextUtils {
    
    private static final int BYTES_IN_MEGABYTE = 1024 * 1024;
    
    private static final int SEC_IN_MIN = 60;
    
    public static String formatSecondsMMss(int seconds){
        return String.format("%02d:%02d", seconds / SEC_IN_MIN, seconds % SEC_IN_MIN);
    }
    
    public static String getStringNullSafe(String string) {
        return isEmpty(string) ? "" : string;
    }
    
    public static String escapeFilePath(String fileName) {
        return fileName.replaceAll("[*?\"\\\\/<>]+", "").replaceAll("\\s+", " ");
    }
    
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

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String join(String... substrings) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String substring : substrings) {
            stringBuffer.append(substring);
        }
        return stringBuffer.toString();
    }

    public static String join(char separator, String... substrings) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String substring : substrings) {
            if (stringBuffer.length() != 0) {
                stringBuffer.append(separator);
            }
            stringBuffer.append(substring);
        }
        return stringBuffer.toString();
    }
    
    public static String formatFileSizeInMb(String format, long size) {
        return String.format(format, (float) size / BYTES_IN_MEGABYTE);
    }
    
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
}
