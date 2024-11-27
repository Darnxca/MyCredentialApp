package com.example.mypasswordmanager.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mypasswordmanager.entita.Credenziali;

import java.util.List;

@Dao
public interface CredenzialiDao {
    // Inserisci un nuovo servizio
    @Insert
    long insertCredenziali(Credenziali credenziali);

    // Aggiorna un servizio esistente
    @Update
    int updateCredenziali(Credenziali credenziali);

    // Elimina un servizio specifico
    @Delete
    int deleteCredenziali(Credenziali credenziali);

    // Elimina tutti i servizi
    @Query("DELETE FROM credenziali")
    void deleteAllCredenziali();

    // Ottiene una credenziale per ID
    @Query("SELECT * FROM credenziali WHERE id = :id")
    Credenziali getCredenzialiById(long id);

    // Recupera tutti i servizi
    @Query("SELECT * FROM credenziali")
    List<Credenziali> getAllCredenziali();
}
