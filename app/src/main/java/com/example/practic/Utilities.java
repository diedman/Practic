package com.example.practic;

import java.nio.charset.StandardCharsets;

public class Utilities {
    static final byte[] HEX_SET = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHexStr(byte[] bytes) {
        byte[] hexBytes = new byte[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexBytes[i * 2] = HEX_SET[v >>> 4];
            hexBytes[i * 2 + 1] = HEX_SET[v & 0x0F];
        }
        return new String(hexBytes, StandardCharsets.UTF_8);
    }
}
