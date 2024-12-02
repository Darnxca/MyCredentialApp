package com.example.mypasswordmanager.ui.aggiornaCredenziali;

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

public class AggiornaViewModel extends ViewModel {

    private final MutableLiveData<String> statoSalvataggio = new MutableLiveData<>();
    private final MutableLiveData<String> nomeServizio = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private int id;

    public AggiornaViewModel() {}

    public LiveData<String> isDataSaved() {
        return statoSalvataggio;
    }

    public LiveData<String> getNomeServizio() {
        return nomeServizio;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public void setCredenziali(Credenziali credenziali) {
        id = credenziali.getId();
        nomeServizio.setValue(credenziali.getServizio());
        username.setValue(credenziali.getUsername());
        password.setValue(credenziali.getPassword());
    }

    public void resetDataSavedMessage() {
        statoSalvataggio.setValue(null);
    }

    public void modifyData(Context context, String nomeServizio, String username, String password) {
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
                credenziali.setId(id);

                long row = database.credenzialiDao().updateCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(Messaggi.MODIFICARIUSCITA.getMessaggio() + "    " + row)
                );
            });
        }
    }
}