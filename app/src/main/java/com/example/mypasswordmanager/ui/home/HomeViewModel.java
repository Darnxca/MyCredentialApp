package com.example.mypasswordmanager.ui.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Credenziali>> mListData;
    private List<Credenziali> fullListData;

    public HomeViewModel() {
        mListData = new MutableLiveData<List<Credenziali>>();
        fullListData = new ArrayList<Credenziali>();
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

    public void filterList(String query) {
        List<Credenziali> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            // Se la ricerca è vuota, mostra tutti gli elementi
            filteredList.addAll(fullListData);
        } else {
            for (Credenziali item : fullListData) {
                // Controlla se il campo contiene la query (case-insensitive)
                if (item.getServizio().toLowerCase().contains(query.toLowerCase()) ||
                        item.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        item.getPassword().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        // Aggiorna i dati filtrati nel LiveData
        mListData.setValue(filteredList);
    }

}
