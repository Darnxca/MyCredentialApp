package com.example.mypasswordmanager.ui.aggiornaCredenziali;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mypasswordmanager.MainActivity;
import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.databinding.FragmentAggiornaCredenzialiBinding;
import com.example.mypasswordmanager.databinding.FragmentDashboardBinding;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.PopUpDialogManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            aggiornaViewModel.setCredenziali(credenziali);
        }

        final EditText nome_servizio = binding.nomeServizio.getEditText();
        final EditText username = binding.username.getEditText();
        final EditText password = binding.password.getEditText();

        aggiornaViewModel.getNomeServizio().observe(getViewLifecycleOwner(), servizio -> Objects.requireNonNull(binding.nomeServizio.getEditText()).setText(servizio));
        aggiornaViewModel.getUsername().observe(getViewLifecycleOwner(), user -> Objects.requireNonNull(binding.username.getEditText()).setText(user));
        aggiornaViewModel.getPassword().observe(getViewLifecycleOwner(), pass -> Objects.requireNonNull(binding.password.getEditText()).setText(pass));

        final Button btn = binding.aggiornaBtn;

        btn.setOnClickListener(v -> {
            aggiornaViewModel.modifyData(Objects.requireNonNull(nome_servizio).getText().toString(),
                    Objects.requireNonNull(username).getText().toString(),
                    Objects.requireNonNull(password).getText().toString());
        });


        aggiornaViewModel.getNomeServizio().observe(getViewLifecycleOwner(), Objects.requireNonNull(nome_servizio)::setText);
        aggiornaViewModel.getUsername().observe(getViewLifecycleOwner(), Objects.requireNonNull(username)::setText);
        aggiornaViewModel.getPassword().observe(getViewLifecycleOwner(), Objects.requireNonNull(password)::setText);

        onBack();

        aggiornaViewModel.isDataSaved().observe(getViewLifecycleOwner(), messaggio -> {
            if (messaggio != null) {
                aggiornaViewModel.resetDataSavedMessage();
               setNavigation();
                if(messaggio.contains(";err"))
                    PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), messaggio.replace(";err", ""));
                else
                    PopUpDialogManager.successPopUp(getContext(), getString(R.string.salvataggio), messaggio);
            }
        });



        return root;
    }


    private void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override    public void handleOnBackPressed() {
                setNavigation();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


    private void setNavigation(){
        NavController navController = NavHostFragment.findNavController(this);
        // Crea le opzioni di navigazione
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_aggiorna_credenziali, true).build();
        // Aggiungere sempre il nuovo fragment a mobile navigation
        navController.navigate(R.id.navigation_home, null, navOptions);
    }

}