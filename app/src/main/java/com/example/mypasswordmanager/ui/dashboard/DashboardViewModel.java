package com.example.mypasswordmanager.ui.dashboard;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.MySecuritySystem;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> statoSalvataggio;

    public DashboardViewModel() {
        statoSalvataggio = new MutableLiveData<>();
    }

    public LiveData<String> isDataSaved() {
        return statoSalvataggio;
    }

    public void resetDataSavedMessage() {
        statoSalvataggio.setValue(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveData(Context context, String nomeServizio, String username, String password) {
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

            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            MySecuritySystem finalMySecuritySystem = mySecuritySystem;

            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(context);

                String servizio_cypher = "", username_cypher = "", password_cypher = "";
                try {
                    servizio_cypher = finalMySecuritySystem.encrypt(nomeServizio);
                    username_cypher = finalMySecuritySystem.encrypt(username);
                    password_cypher = finalMySecuritySystem.encrypt(password);

                } catch (Exception e) {
                    // Aggiorna LiveData sul main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            statoSalvataggio.setValue(context.getString(R.string.erroreCifratura) + ";err")
                    );
                }

                Credenziali credenziali = new Credenziali(servizio_cypher, username_cypher, password_cypher);
                database.credenzialiDao().insertCredenziali(credenziali);

                // Aggiorna LiveData sul main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        statoSalvataggio.setValue(context.getString(R.string.credenzialisalvate))
                );
            });
        }
    }
}