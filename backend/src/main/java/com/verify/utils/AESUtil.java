package com.verify.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtil {

    private static final String ALGO = "AES/CBC/PKCS5Padding";

    private static SecretKeySpec getKey(String secret) {
        byte[] key = new byte[32];
        byte[] secretBytes = secret.getBytes();

        System.arraycopy(secretBytes, 0, key, 0,
                Math.min(secretBytes.length, key.length));

        return new SecretKeySpec(key, "AES");
    }

    public static byte[] encrypt(byte[] data, String secret) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGO);

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, getKey(secret), new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(data);

        byte[] result = new byte[iv.length + encrypted.length];

        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        return result;
    }

    public static byte[] decrypt(byte[] data, String secret) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGO);

        byte[] iv = new byte[16];
        byte[] actual = new byte[data.length - 16];

        System.arraycopy(data, 0, iv, 0, 16);
        System.arraycopy(data, 16, actual, 0, actual.length);

        cipher.init(Cipher.DECRYPT_MODE, getKey(secret), new IvParameterSpec(iv));

        return cipher.doFinal(actual);
    }
}
