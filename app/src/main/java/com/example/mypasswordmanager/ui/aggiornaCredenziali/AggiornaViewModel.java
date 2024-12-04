package com.example.mypasswordmanager.ui.aggiornaCredenziali;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.mykeystore.MySecuritySystem;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AggiornaViewModel extends AndroidViewModel {

    private final MutableLiveData<String> statoSalvataggio;
    private final MutableLiveData<String> nomeServizio;
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> password;
    private final Context context;
    private int id;

    public AggiornaViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.statoSalvataggio = new MutableLiveData<>();
        this.nomeServizio = new MutableLiveData<>();
        this.username = new MutableLiveData<>();
        this.password = new MutableLiveData<>();


    }

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

        MySecuritySystem mySecuritySystem = null;
        try {
            mySecuritySystem = MySecuritySystem.getInstance();

        } catch (Exception e) {
            statoSalvataggio.setValue(this.context.getString(R.string.errore) + ";err");
        }

        try {
            nomeServizio.setValue(Objects.requireNonNull(mySecuritySystem).decrypt(credenziali.getServizio()));
            username.setValue(mySecuritySystem.decrypt(credenziali.getUsername()));
            password.setValue(mySecuritySystem.decrypt(credenziali.getPassword()));
        } catch (Exception e) {
            statoSalvataggio.setValue(this.context.getString(R.string.erroreDecriptazione) + ";err");
        }

    }

    public void resetDataSavedMessage() {
        statoSalvataggio.setValue(null);
    }

    public void modifyData(String nomeServizio, String username, String password) {
        if (nomeServizio.isEmpty()) {
            statoSalvataggio.setValue(this.context.getString(R.string.campo_servizio_vuoto) + ";err");
        } else if (username.isEmpty()) {
            statoSalvataggio.setValue(this.context.getString(R.string.campo_username_vuoto) + ";err");
        } else if (password.isEmpty()) {
            statoSalvataggio.setValue(this.context.getString(R.string.campo_password_vuoto) + ";err");
        }else {

            MySecuritySystem mySecuritySystem = null;
            try {
                mySecuritySystem = MySecuritySystem.getInstance();

            } catch (Exception e) {
                statoSalvataggio.setValue(this.context.getString(R.string.errore) + ";err");
            }
            MySecuritySystem finalMySecuritySystem = mySecuritySystem;

            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(this.context);

                String servizio_cypher = "", username_cypher = "", password_cypher = "";
                try {
                    servizio_cypher = finalMySecuritySystem.encrypt(nomeServizio);
                    username_cypher = finalMySecuritySystem.encrypt(username);
                    password_cypher = finalMySecuritySystem.encrypt(password);

                } catch (Exception e) {
                    statoSalvataggio.setValue(this.context.getString(R.string.erroreCifratura) + ";err");
                }

                Credenziali credenziali = new Credenziali(servizio_cypher, username_cypher, password_cypher);
                credenziali.setId(id);

                database.credenzialiDao().updateCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(this.context.getString(R.string.modificariuscita))
                );
            });
        }
    }
}