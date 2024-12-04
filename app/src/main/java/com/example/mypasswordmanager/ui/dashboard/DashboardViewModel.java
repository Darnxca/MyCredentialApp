package com.example.mypasswordmanager.ui.dashboard;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.mykeystore.MySecuritySystem;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<String> statoSalvataggio;
    private final MutableLiveData<String> nomeServizio;
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> password;
    private final Context context;


    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.statoSalvataggio = new MutableLiveData<>();
        this.context = application.getApplicationContext();
        this.nomeServizio = new MutableLiveData<>();
        this.username = new MutableLiveData<>();
        this.password = new MutableLiveData<>();
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

    public LiveData<String> isDataSaved() {
        return statoSalvataggio;
    }


    public void emptyCredenziali(){
        this.nomeServizio.setValue("");
        this.username.setValue("");
        this.password.setValue("");
    }
    public void resetDataSavedMessage() {
        statoSalvataggio.setValue(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveData(String nomeServizio, String username, String password) {
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

            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            MySecuritySystem finalMySecuritySystem = mySecuritySystem;

            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(this.context);

                String servizio_cypher = "", username_cypher = "", password_cypher = "";
                try {
                    servizio_cypher = finalMySecuritySystem.encrypt(nomeServizio);
                    username_cypher = finalMySecuritySystem.encrypt(username);
                    password_cypher = finalMySecuritySystem.encrypt(password);

                } catch (Exception e) {
                    // Aggiorna LiveData sul main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            statoSalvataggio.setValue(this.context.getString(R.string.erroreCifratura) + ";err")
                    );
                }

                Credenziali credenziali = new Credenziali(servizio_cypher, username_cypher, password_cypher);
                database.credenzialiDao().insertCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(this.context.getString(R.string.credenzialisalvate))
                );
            });
        }
    }
}