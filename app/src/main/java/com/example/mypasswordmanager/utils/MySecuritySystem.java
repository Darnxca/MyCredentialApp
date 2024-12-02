package com.example.mypasswordmanager.utils;


import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MySecuritySystem {
    private static final String KEY_ALIAS = "my_secret_key";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private KeyStore keyStore;

    public MySecuritySystem() throws Exception {
        keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        keyStore.load(null);
    }

    public SecretKey getSecretKey() throws Exception {
        KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);
        if (entry == null) {
            return generateKey();
        } else {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        }
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);
        keyGenerator.init(
                new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());
        return keyGenerator.generateKey();
    }

    public String encrypt(String plaintext) throws Exception {
        SecretKey secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();
        byte[] encryption = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDataWithIv = new byte[iv.length + encryption.length];
        System.arraycopy(iv, 0, encryptedDataWithIv, 0, iv.length);
        System.arraycopy(encryption, 0, encryptedDataWithIv, iv.length, encryption.length);

        return Base64.encodeToString(encryptedDataWithIv, Base64.NO_WRAP);
    }

    public String decrypt(String encryptedData) throws Exception {
        SecretKey secretKey = getSecretKey();

        byte[] encryptedBytes = Base64.decode(encryptedData, Base64.NO_WRAP);
        byte[] iv = new byte[12]; // GCM IV is 12 bytes
        byte[] cipherText = new byte[encryptedBytes.length - iv.length];

        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        System.arraycopy(encryptedBytes, iv.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        byte[] decrypted = cipher.doFinal(cipherText);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
