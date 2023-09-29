package utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataHash {
    public static String encodeWord(String word) {
        try {
            MessageDigest message = MessageDigest.getInstance("SHA-1");
            byte[] dig = message.digest(word.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, dig);
            String encodeString = num.toString(16);
            while (encodeString.length() < 32) {
                encodeString = "0" + encodeString;
            }
            return encodeString;
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}