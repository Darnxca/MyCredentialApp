package com.example.mypasswordmanager;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import com.example.mypasswordmanager.DAO.CredenzialiDao;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;

public class AppDatabaseTest {

    private AppDatabase database;
    private CredenzialiDao credenzialiDao;

    @Before
    public void setUp() {
        // Creare un'istanza del database in memoria per i test
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class
        ).allowMainThreadQueries().build();

        // Ottenere il DAO
        credenzialiDao = database.credenzialiDao();
    }

    @After
    public void tearDown() {
        // Chiudere il database dopo i test
        database.close();
    }

    @Test
    public void testInsertAndRetrieveService() {
        // Inserire una credenziale
        Credenziali credenziali = new Credenziali("Google", "asfnazza", "password123");
        long id = credenzialiDao.insertCredenziali(credenziali);

        // Recuperare Le credenziali appena inserite
        Credenziali credenzialiCercate = credenzialiDao.getCredenzialiById(id);

        // Verificare che sia stato inserito
        assertEquals(id, credenzialiCercate.getId());
        assertEquals("Google", credenzialiCercate.getServizio());
        assertEquals("asfnazza", credenzialiCercate.getUsername());
        assertEquals("password123", credenzialiCercate.getPassword());
    }

    @Test
    public void testUpdateService() {
        // Inserire una credenziale
        Credenziali credenziali = new Credenziali("Google", "asfnazza", "password123");
        long id = credenzialiDao.insertCredenziali(credenziali);

        // Recuperare Le credenziali appena inserite
        Credenziali credenzialiCercate = credenzialiDao.getCredenzialiById(id);

        // Aggiornare il servizio
        credenzialiCercate.setServizio("Facebook");
        credenzialiCercate.setPassword("newPassword123");
        int rowsUpdated = credenzialiDao.updateCredenziali(credenzialiCercate);

        // Recuperare Le credenziali appena inserite
        Credenziali credenzialiAggiornate = credenzialiDao.getCredenzialiById(id);

        // Verificare l'aggiornamento
        assertEquals(1, rowsUpdated);
        assertEquals("Facebook", credenzialiAggiornate.getServizio());
        assertEquals("newPassword123", credenzialiAggiornate.getPassword());
    }

    @Test
    public void testDeleteService() {
        // Inserire una credenziale
        Credenziali credenziali = new Credenziali("Google", "asfnazza", "password123");
        long id = credenzialiDao.insertCredenziali(credenziali);

        // Recuperare Le credenziali appena inserite
        Credenziali credenzialiCercate = credenzialiDao.getCredenzialiById(id);

        // Eliminare il servizio
        int rowsDeleted = credenzialiDao.deleteCredenziali(credenzialiCercate);

        // Recuperare tutti i servizi
        List<Credenziali> services = credenzialiDao.getAllCredenziali();

        // Verificare che sia stato eliminato
        assertEquals(1, rowsDeleted);
        assertTrue(services.isEmpty());
    }

    @Test
    public void testDeleteAllServices() {
        // Inserire più servizi
        credenzialiDao.insertCredenziali(new Credenziali("Google", "aaa", "password123"));
        credenzialiDao.insertCredenziali(new Credenziali("Facebook", "bbb", "password456"));

        // Eliminare tutti i servizi
        credenzialiDao.deleteAllCredenziali();

        // Verificare che la tabella sia vuota
        List<Credenziali> services = credenzialiDao.getAllCredenziali();
        assertTrue(services.isEmpty());
    }
}
