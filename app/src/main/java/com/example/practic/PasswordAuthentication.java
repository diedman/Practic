package com.example.practic;

import static com.example.practic.Utilities.bytesToHexStr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class PasswordAuthentication
{
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
        String salt = hashedOriginal.substring(0, hashedOriginal.indexOf(":"));

        return getHashedPassword(password, salt).equals(hashedOriginal);
    }
}