package com.example.mypasswordmanager.ui.impostazioni;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mypasswordmanager.databinding.FragmentImpostazioniBinding;

import java.util.Objects;

public class ImpostazioniFragment extends Fragment {

    private FragmentImpostazioniBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        impostazioniViewModel impostazioniViewModel =
                new ViewModelProvider(this).get(impostazioniViewModel.class);

        binding = FragmentImpostazioniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final SwitchCompat darkModeSwitch = binding.darkMode;

        impostazioniViewModel.isChecked().observe(getViewLifecycleOwner(),darkModeSwitch::setChecked);


        darkModeSwitch.setOnCheckedChangeListener(impostazioniViewModel::changeTheme);


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