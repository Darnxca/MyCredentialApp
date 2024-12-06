package com.example.mypasswordmanager;

import static org.junit.Assert.assertEquals;

import com.example.mypasswordmanager.entita.Credenziali;

import org.junit.Before;
import org.junit.Test;

public class MyCypherUnitTest {
    private Credenziali credenziali1, credenziali2;

    @Before
    public void setUp() {
        credenziali1 = new Credenziali();
        credenziali2 = new Credenziali("Google", "Asfnazza", "1234");
    }

    @Test
    public void testSetterGetter() {
        credenziali2.setId(1);
        credenziali1.setId(1);

        credenziali1.setServizio(credenziali2.getServizio());
        credenziali1.setUsername(credenziali2.getUsername());
        credenziali1.setPassword(credenziali2.getPassword());

        assertEquals(credenziali1.getId(), credenziali2.getId());
        assertEquals(credenziali1.getServizio(), credenziali2.getServizio());
        assertEquals(credenziali1.getUsername(), credenziali2.getUsername());
        assertEquals(credenziali1.getPassword(), credenziali2.getPassword());
    }

    @Test
    public void testToString() {
        String example = "Credenziali{id=0, servizio='Google', username='Asfnazza', password='1234'}";
        assertEquals(example, credenziali2.toString());
    }

}
