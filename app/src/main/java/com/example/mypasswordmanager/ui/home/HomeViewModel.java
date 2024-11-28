package com.example.mypasswordmanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<String[]>> mListData;

    public HomeViewModel() {
        mListData = new MutableLiveData<>();
        loadListData();
    }

    // Metodo per caricare dati fittizi (o da una fonte esterna)
    private void loadListData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Item 1A", "Item 1B", "Item 1C", "Item 1D"});
        data.add(new String[]{"Item 2A", "Item 2B", "Item 2C", "Item 2D"});
        data.add(new String[]{"Item 3A", "Item 3B", "Item 3C", "Item 3D"});
        mListData.setValue(data);
    }

    public LiveData<List<String[]>> getListData() {
        return mListData;
    }
}
