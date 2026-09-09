package cc.nuym.jnic.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    public static String encrypt(final byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decrypt(final String str) {
        return Base64.getDecoder().decode(str.getBytes(StandardCharsets.US_ASCII));
    }
}
