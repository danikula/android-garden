package com.danikula.android.garden.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    
    private static final int BYTES_IN_MEGABYTE = 1024 * 1024;
    
    private static final int SEC_IN_MIN = 60;
    
    public static String formatSecondsMMss(int seconds){
        return String.format("%02d:%02d", seconds / SEC_IN_MIN, seconds % SEC_IN_MIN);
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

    public static String formatFileSizeInMb(String format, long size) {
        return String.format(format, (float) size / BYTES_IN_MEGABYTE);
    }
}
