package com.example.mypasswordmanager.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.adapter.CredenzialiRecyclerAdapter;
import com.example.mypasswordmanager.databinding.FragmentHomeBinding;
import com.example.mypasswordmanager.utils.PopUpDialogManager;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.loadListData();

        onBack();

        LinearLayout button_categoria = binding.buttonContainer;

        homeViewModel.getListCategorie().observe(getViewLifecycleOwner(), data -> {
            data.forEach(categoria -> {
                button_categoria.addView(homeViewModel.aggiungiBottoneCategoria(getContext(), categoria));
            });
        });

        // Configurazione RecyclerView
        RecyclerView recyclerView = binding.recyclerViewCredenziali;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Imposta un adapter vuoto, che verrà popolato in seguito
        CredenzialiRecyclerAdapter credenzialiRecyclerAdapter= new CredenzialiRecyclerAdapter(List.of(), getContext(), this); // Adapter inizialmente vuoto
        recyclerView.setAdapter(credenzialiRecyclerAdapter);

        // Osserva i dati nel ViewModel
        // Quando i dati cambiano, aggiorna l'adapter
        homeViewModel.getListData().observe(getViewLifecycleOwner(), credenzialiRecyclerAdapter::updateData);

        homeViewModel.isData().observe(getViewLifecycleOwner(), messaggio -> {
            if (messaggio != null) {
                PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), messaggio);
                homeViewModel.resetDataMessage();
            }
        });

        // Impostare il filtro per la SearchView
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Non è necessario fare nulla quando l'utente preme invio
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra i dati ogni volta che la query cambia
                homeViewModel.filterList(newText);
                return true;
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override    public void handleOnBackPressed() {}
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

}