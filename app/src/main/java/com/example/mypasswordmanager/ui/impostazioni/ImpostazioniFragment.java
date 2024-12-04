package com.example.mypasswordmanager.ui.impostazioni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mypasswordmanager.databinding.FragmentImpostazioniBinding;

public class ImpostazioniFragment extends Fragment {

    private FragmentImpostazioniBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        impostazioniViewModel impostazioniViewModel =
                new ViewModelProvider(this).get(impostazioniViewModel.class);

        binding = FragmentImpostazioniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        final TextView textView = binding.textNotifications;
        impostazioniViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}