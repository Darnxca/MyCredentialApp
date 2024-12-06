package com.example.mypasswordmanager;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import com.example.mypasswordmanager.utils.MyCypher;

public class CredenzialiUnitTest {

    private String passphrase;

    @Before
    public void setUp() {
        passphrase = "carmaine";
    }

    @Test
    public void testEncrypt() {
        try {
            String dataEncrypt = MyCypher.encrypt("casa", passphrase);
            assertEquals("88fU+DpvTyO/6bUu5xumcg==", dataEncrypt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDecrypt() {
        try {
            String dataEncrypt = MyCypher.decrypt("88fU+DpvTyO/6bUu5xumcg==", passphrase);
            assertEquals("casa", dataEncrypt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
