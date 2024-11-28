package com.example.mypasswordmanager.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Configurazione RecyclerView
        RecyclerView recyclerView = binding.recyclerViewCredenziali;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Imposta un adapter vuoto, che verrà popolato in seguito
        CredenzialiRecyclerAdapter credenzialiRecyclerAdapter= new CredenzialiRecyclerAdapter(List.of()); // Adapter inizialmente vuoto
        recyclerView.setAdapter(credenzialiRecyclerAdapter);

        // Osserva i dati nel ViewModel
        homeViewModel.getListData().observe(getViewLifecycleOwner(), data -> {
            // Quando i dati cambiano, aggiorna l'adapter
            credenzialiRecyclerAdapter.updateData(data);
        });







        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}