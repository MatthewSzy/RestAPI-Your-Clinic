package com.surgery.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    private PasswordHash(){}

    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        String algorithm = "SHA-256";
        return generateHash(password, algorithm);
    }

    private static String generateHash(String password, String algorithm) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();

        byte[] hashBytes = messageDigest.digest(password.getBytes());
        return bytesToStringHex(hashBytes);
    }

    private static String bytesToStringHex(byte[] bytes) {

        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {

            int byteValue = bytes[i] & 0xFF;

            hexChars[2 * i] = hexArray[byteValue >>> 4];
            hexChars[2 * i + 1] = hexArray[byteValue & 0x0F];
        }

        return new String(hexChars);
    }
}
