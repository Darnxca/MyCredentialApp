package com.example.mypasswordmanager.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.databinding.FragmentDashboardBinding;
import com.example.mypasswordmanager.utils.PopUpDialogManager;
import com.example.mypasswordmanager.utils.Utils;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView imageView = binding.image;
        final EditText nome_servizio = binding.nomeServizio.getEditText();
        final EditText username = binding.username.getEditText();
        final EditText password = binding.password.getEditText();

        final Button btn = binding.aggiungiBtn;

        imageView.setImageResource(Utils.getImgFromShared(requireContext()));

        btn.setOnClickListener(v -> {
            dashboardViewModel.saveData(Objects.requireNonNull(nome_servizio).getText().toString(),
                    Objects.requireNonNull(username).getText().toString(),
                    Objects.requireNonNull(password).getText().toString());
        });

        dashboardViewModel.getNomeServizio().observe(getViewLifecycleOwner(), Objects.requireNonNull(nome_servizio)::setText);
        dashboardViewModel.getUsername().observe(getViewLifecycleOwner(), Objects.requireNonNull(username)::setText);
        dashboardViewModel.getPassword().observe(getViewLifecycleOwner(), Objects.requireNonNull(password)::setText);


        dashboardViewModel.isDataSaved().observe(getViewLifecycleOwner(), messaggio -> {
            if (messaggio != null) {
                if(messaggio.contains(";err"))
                    PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), messaggio.replace(";err", ""));
                else {
                    PopUpDialogManager.successPopUp(getContext(), getString(R.string.salvataggio), messaggio);
                    dashboardViewModel.emptyCredenziali();
                }
                dashboardViewModel.resetDataSavedMessage();
            }
        });

        onBack();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                .setPopUpTo(R.id.navigation_dashboard, true).build();
        // Aggiungere sempre il nuovo fragment a mobile navigation
        navController.navigate(R.id.navigation_home, null, navOptions);
    }



}