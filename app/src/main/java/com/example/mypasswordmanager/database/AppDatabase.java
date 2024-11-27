package com.example.mypasswordmanager.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.mypasswordmanager.DAO.CredenzialiDao;
import com.example.mypasswordmanager.entita.Credenziali;

@Database(entities = {Credenziali.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Riferimento al DAO
    public abstract CredenzialiDao credenzialiDao();

    // Singleton per evitare istanze multiple del database
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "credenziali_database")
                            .fallbackToDestructiveMigration() // Ricrea il database in caso di aggiornamenti
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
