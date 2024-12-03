package com.example.mypasswordmanager.ui.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.MySecuritySystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Credenziali>> mListData;
    private List<Credenziali> fullListData;
    private final MutableLiveData<String> stato;

    public HomeViewModel() {
        mListData = new MutableLiveData<>();
        fullListData = new ArrayList<>();
        stato = new MutableLiveData<>();
    }

    public LiveData<String> isData() {
        return stato;
    }

    public void resetDataMessage() {
        stato.setValue(null);
    }

    protected void loadListData(Context context) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Ottieni l'istanza del database
            AppDatabase database = AppDatabase.getInstance(context);

            // Recupera le credenziali dal database
            List<Credenziali> data = database.credenzialiDao().getAllCredenziali();

            // Aggiorna `fullListData` e `mListData` sul main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                fullListData = data;
                mListData.setValue(data);
            });
        });
    }

    // Restituisce i dati
    public LiveData<List<Credenziali>> getListData() {
        return mListData;
    }

    public void filterList(String query, Context context) {
        List<Credenziali> filteredList = new ArrayList<>();
        MySecuritySystem mySecuritySystem = null;

        try {
            mySecuritySystem = new MySecuritySystem();

        } catch (Exception e) {
            stato.setValue(context.getString(R.string.errore)+ ";err");
        }

        if (query.isEmpty()) {
            // Se la ricerca è vuota, mostra tutti gli elementi
            filteredList.addAll(fullListData);
        } else {
            for (Credenziali item : fullListData) {
                // Controlla se il campo contiene la query (case-insensitive)
                try {
                    if (Objects.requireNonNull(mySecuritySystem).decrypt(item.getServizio()).toLowerCase().contains(query.toLowerCase()) ||
                            mySecuritySystem.decrypt(item.getUsername()).toLowerCase().contains(query.toLowerCase()) ||
                            mySecuritySystem.decrypt(item.getPassword()).toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                } catch (Exception e) {
                    stato.setValue(context.getString(R.string.erroreDecriptazione)+ ";err");
                }
            }
        }
        // Aggiorna i dati filtrati nel LiveData
        mListData.setValue(filteredList);
    }

}
