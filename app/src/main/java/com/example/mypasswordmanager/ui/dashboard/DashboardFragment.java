package com.example.mypasswordmanager.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mypasswordmanager.databinding.FragmentDashboardBinding;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText nome_servizio = binding.nomeServizio.getEditText();
        final EditText username = binding.username.getEditText();
        final EditText password = binding.password.getEditText();

        final Button btn = binding.aggiungiBtn;

        btn.setOnClickListener(v -> {
            dashboardViewModel.saveData(getContext(),Objects.requireNonNull(nome_servizio).getText().toString(),
                    Objects.requireNonNull(username).getText().toString(),
                    Objects.requireNonNull(password).getText().toString());
        });


        dashboardViewModel.isDataSaved().observe(getViewLifecycleOwner(), messaggio -> {
            Toast.makeText(getContext(), messaggio, Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}