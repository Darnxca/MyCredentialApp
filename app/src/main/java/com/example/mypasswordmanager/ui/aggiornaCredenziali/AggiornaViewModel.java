package com.example.mypasswordmanager.ui.aggiornaCredenziali;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.MySecuritySystem;

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

    public void setCredenziali(Credenziali credenziali, Context context) {
        id = credenziali.getId();

        MySecuritySystem mySecuritySystem = null;
        try {
            mySecuritySystem = new MySecuritySystem();

        } catch (Exception e) {
            statoSalvataggio.setValue(context.getString(R.string.errore) + ";err");
        }

        try {
            nomeServizio.setValue(mySecuritySystem.decrypt(credenziali.getServizio()));
            username.setValue(mySecuritySystem.decrypt(credenziali.getUsername()));
            password.setValue(mySecuritySystem.decrypt(credenziali.getPassword()));
        } catch (Exception e) {
            statoSalvataggio.setValue(context.getString(R.string.erroreDecriptazione) + ";err");
        }

    }

    public void resetDataSavedMessage() {
        statoSalvataggio.setValue(null);
    }

    public void modifyData(Context context, String nomeServizio, String username, String password) {
        if (nomeServizio.isEmpty()) {
            statoSalvataggio.setValue(context.getString(R.string.campo_servizio_vuoto) + ";err");
        } else if (username.isEmpty()) {
            statoSalvataggio.setValue(context.getString(R.string.campo_username_vuoto) + ";err");
        } else if (password.isEmpty()) {
            statoSalvataggio.setValue(context.getString(R.string.campo_password_vuoto) + ";err");
        }else {

            MySecuritySystem mySecuritySystem = null;
            try {
                mySecuritySystem = new MySecuritySystem();

            } catch (Exception e) {
                statoSalvataggio.setValue(context.getString(R.string.errore) + ";err");
            }
            MySecuritySystem finalMySecuritySystem = mySecuritySystem;

            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(context);

                String servizio_cypher = "", username_cypher = "", password_cypher = "";
                try {
                    servizio_cypher = finalMySecuritySystem.encrypt(nomeServizio);
                    username_cypher = finalMySecuritySystem.encrypt(username);
                    password_cypher = finalMySecuritySystem.encrypt(password);

                } catch (Exception e) {
                    statoSalvataggio.setValue(context.getString(R.string.erroreCifratura) + ";err");
                }

                Credenziali credenziali = new Credenziali(servizio_cypher, username_cypher, password_cypher);
                credenziali.setId(id);

                database.credenzialiDao().updateCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(context.getString(R.string.modificariuscita))
                );
            });
        }
    }
}