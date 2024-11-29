package com.example.mypasswordmanager.ui.dashboard;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.Messaggi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> statoSalvataggio = new MutableLiveData<>();

    public DashboardViewModel() {

    }

    public LiveData<String> isDataSaved() {
        return statoSalvataggio;
    }

    public void saveData(Context context, String nomeServizio, String username, String password) {
        if (nomeServizio.isEmpty()) {
            statoSalvataggio.setValue(Messaggi.CAMPO_NOME_SERVIZIO_VUOTO.getMessaggio());
        } else if (username.isEmpty()) {
            statoSalvataggio.setValue(Messaggi.CAMPO_USERNAME_VUOTO.getMessaggio());
        } else if (password.isEmpty()) {
            statoSalvataggio.setValue(Messaggi.CAMPO_PASSWORD_VUOTO.getMessaggio());
        }else {

            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(context);
                Credenziali credenziali = new Credenziali(nomeServizio, username, password);
                long id = database.credenzialiDao().insertCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(Messaggi.SALVATAGGIO_RIUSCITO.getMessaggio() + "    " + id)
                );
            });
        }
    }
}