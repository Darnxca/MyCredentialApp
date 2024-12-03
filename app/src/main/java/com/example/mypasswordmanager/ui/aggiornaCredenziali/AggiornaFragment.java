package com.example.mypasswordmanager.ui.aggiornaCredenziali;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.databinding.FragmentAggiornaCredenzialiBinding;
import com.example.mypasswordmanager.databinding.FragmentDashboardBinding;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.PopUpDialogManager;

import java.util.Objects;

public class AggiornaFragment extends Fragment {

    private FragmentAggiornaCredenzialiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AggiornaViewModel aggiornaViewModel =
                new ViewModelProvider(this).get(AggiornaViewModel.class);

        binding = FragmentAggiornaCredenzialiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Recupera i dati
        Credenziali credenziali = (Credenziali) requireArguments().getSerializable("credenziali");

        if (credenziali != null) {
            // Usa l'oggetto Credenziali (ad esempio, riempi i campi del form)
            aggiornaViewModel.setCredenziali(credenziali, getContext());
        }

        final EditText nome_servizio = binding.nomeServizio.getEditText();
        final EditText username = binding.username.getEditText();
        final EditText password = binding.password.getEditText();

        aggiornaViewModel.getNomeServizio().observe(getViewLifecycleOwner(), servizio -> Objects.requireNonNull(binding.nomeServizio.getEditText()).setText(servizio));
        aggiornaViewModel.getUsername().observe(getViewLifecycleOwner(), user -> Objects.requireNonNull(binding.username.getEditText()).setText(user));
        aggiornaViewModel.getPassword().observe(getViewLifecycleOwner(), pass -> Objects.requireNonNull(binding.password.getEditText()).setText(pass));

        final Button btn = binding.aggiornaBtn;

        btn.setOnClickListener(v -> {
            aggiornaViewModel.modifyData(getContext(),Objects.requireNonNull(nome_servizio).getText().toString(),
                    Objects.requireNonNull(username).getText().toString(),
                    Objects.requireNonNull(password).getText().toString());
        });


        aggiornaViewModel.isDataSaved().observe(getViewLifecycleOwner(), messaggio -> {
            if (messaggio != null) {
                aggiornaViewModel.resetDataSavedMessage();
                requireActivity().getSupportFragmentManager().popBackStack();
                if(messaggio.contains(";err"))
                    PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), messaggio.replace(";err", ""));
                else
                    PopUpDialogManager.successPopUp(getContext(), getString(R.string.salvataggio), messaggio);
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