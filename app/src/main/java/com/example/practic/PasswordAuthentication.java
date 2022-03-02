package com.example.practic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class PasswordAuthentication
{
    static final byte[] HEX_SET = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);
    private static String bytesToHexStr(byte[] bytes) {
        byte[] hexBytes = new byte[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexBytes[i * 2] = HEX_SET[v >>> 4];
            hexBytes[i * 2 + 1] = HEX_SET[v & 0x0F];
        }
        return new String(hexBytes, StandardCharsets.UTF_8);
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);

        return bytesToHexStr(salt);
    }

    public static String getHashedPassword(String password) {
        String hashedPassword = null;
        try {
            String salt = getSalt();
            hashedPassword = getHashedPassword(password, salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static String getHashedPassword(String password, String salt) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            md.update(salt.getBytes());
            byte[] bytes = md.digest();
            hashedPassword = salt + ":" + bytesToHexStr(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static boolean authenticate(String password, String hashedOriginal) {
        String salt = hashedOriginal.substring(0, hashedOriginal.indexOf(":") - 1);

        return getHashedPassword(password, salt).equals(hashedOriginal);
    }
}