package com.example.mypasswordmanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<String[]>> mListData;
    private List<String[]> fullListData;

    public HomeViewModel() {
        mListData = new MutableLiveData<>();
        fullListData = new ArrayList<>();
        loadListData();
    }

    // Metodo per caricare dati fittizi (o da una fonte esterna)
    private void loadListData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"0","Item 1A", "Item 1B", "Item 1C", "Item 1D"});
        data.add(new String[]{"1","Item 2A", "Item 2B", "Item 2C", "Item 2D"});
        data.add(new String[]{"2","Item 3A", "Item 3B", "Item 3C", "Item 3D"});
        data.add(new String[]{"3","Item 4A", "Item 4B", "Item 4C", "Item 4D"});
        fullListData = data;
        mListData.setValue(data);
    }

    // Restituisce i dati
    public LiveData<List<String[]>> getListData() {
        return mListData;
    }

    // Metodo per filtrare i dati in base alla ricerca
    public void filterList(String query) {
        List<String[]> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(fullListData);  // Se la ricerca è vuota, mostra tutti gli elementi
        } else {
            for (String[] item : fullListData) {
                for (String field : item) {
                    if (field.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);  // Aggiungi gli elementi che corrispondono alla ricerca
                        break;  // Uscire dal ciclo interno appena trovi una corrispondenza
                    }
                }
            }
        }
        mListData.setValue(filteredList);  // Notifica che i dati sono cambiati
    }
}
