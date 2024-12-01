package com.example.mypasswordmanager.ui.home;

import static android.content.Context.CLIPBOARD_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.adapter.CredenzialiRecyclerAdapter;
import com.example.mypasswordmanager.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.loadListData(getContext());

        // Configurazione RecyclerView
        RecyclerView recyclerView = binding.recyclerViewCredenziali;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Imposta un adapter vuoto, che verrà popolato in seguito
        CredenzialiRecyclerAdapter credenzialiRecyclerAdapter= new CredenzialiRecyclerAdapter(List.of(), getContext(), this); // Adapter inizialmente vuoto
        recyclerView.setAdapter(credenzialiRecyclerAdapter);

        // Osserva i dati nel ViewModel
        homeViewModel.getListData().observe(getViewLifecycleOwner(), data -> {
            // Quando i dati cambiano, aggiorna l'adapter
            credenzialiRecyclerAdapter.updateData(data);
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
}