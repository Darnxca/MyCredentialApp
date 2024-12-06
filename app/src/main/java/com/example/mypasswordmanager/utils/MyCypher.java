package com.example.mypasswordmanager.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MyCypher {

    private static SecretKeySpec getKeyFromPassphrase(String passphrase) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(passphrase.getBytes());
        return new SecretKeySpec(bytes, "AES");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String data, String passphrase) throws Exception {
        SecretKeySpec key = getKeyFromPassphrase(passphrase);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String encryptedData, String passphrase) throws Exception {
        SecretKeySpec key = getKeyFromPassphrase(passphrase);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);

        return new String(decryptedData);
    }
}
