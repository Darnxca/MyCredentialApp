package com.example.mypasswordmanager.ui.impostazioni;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.databinding.FragmentImpostazioniBinding;
import com.example.mypasswordmanager.utils.Utils;
import com.example.mypasswordmanager.utils.dialog.MyCustomDialogImage;
import com.example.mypasswordmanager.utils.dialog.MyCustomDialogPassphrase;
import com.example.mypasswordmanager.utils.PassphraseCallback;
import com.example.mypasswordmanager.utils.PopUpDialogManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ImpostazioniFragment extends Fragment implements PassphraseCallback {

    private FragmentImpostazioniBinding binding;
    private impostazioniViewModel impostazioniViewModel;
    private String encryptData;
    private Boolean download = true;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        impostazioniViewModel =
                new ViewModelProvider(this).get(impostazioniViewModel.class);

        binding = FragmentImpostazioniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final SwitchCompat darkModeSwitch = binding.darkMode;
        final ImageButton downloadButton = binding.download;
        final ImageButton uploadButton = binding.upload;
        final ImageButton selectImage = binding.selectImg;


        selectImage.setOnClickListener(v -> MyCustomDialogImage.showImageSelectorDialog(requireContext()));

        impostazioniViewModel.isChecked().observe(getViewLifecycleOwner(),darkModeSwitch::setChecked);

        darkModeSwitch.setOnCheckedChangeListener(impostazioniViewModel::changeTheme);

        downloadButton.setOnClickListener( v -> {
            download = true;
            if(!impostazioniViewModel.getPassphrase().isEmpty()) {
                impostazioniViewModel.scaricaCredenziali();
            } else {
                MyCustomDialogPassphrase.openPassphraseDialog(this.getActivity(),this);
            }
        });

        uploadButton.setOnClickListener( v -> {
            download = false;
            if(!impostazioniViewModel.getPassphrase().isEmpty()) {
                chooseFileToRead();
            } else {
                MyCustomDialogPassphrase.openPassphraseDialog(this.getActivity(),this);
            }
        });

        impostazioniViewModel.isData().observe(getViewLifecycleOwner(), messaggio -> {
            if (messaggio != null) {
                if(messaggio.contains(";err")) {
                    PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), messaggio.replace(";err", ""));
                    impostazioniViewModel.setPassphrase("");
                }
                else {
                    PopUpDialogManager.successPopUp(getContext(), getString(R.string.salvataggio), messaggio);
                }
                impostazioniViewModel.resetDataMessage();

            }

        });

        impostazioniViewModel.getEncrypt().observe(getViewLifecycleOwner(), data -> {
                if(!data.isEmpty()) {
                    encryptData = data;
                    chooseFileLocation();
                }
            }
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onPassphraseEntered(String passphrase) {
        impostazioniViewModel.setPassphrase(passphrase);
        if(download)
            impostazioniViewModel.scaricaCredenziali();
        else
            chooseFileToRead();
    }


    // Avvia l'Intent per far scegliere all'utente il percorso dove salvare il file
    private void chooseFileLocation() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "my_credentials.txt");  // Imposto un nome di file predefinito
        // Uso ActivityResultLauncher per gestire il risultato
        startForResult.launch(intent);
    }

    // Utilizzo di ActivityResultLauncher per gestire cosa fare quando l'utente ha scelto dove salvare il file
    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = Objects.requireNonNull(result.getData()).getData();
                        if (uri != null) {
                            writeToFile(uri, encryptData);
                        }
                    } else {
                        PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), getString(R.string.fallimento_selezione_file));
                    }
                }
            });

    // Scrivere il contenuto nel file selezionato
    private void writeToFile(Uri uri, String content) {
        try (OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                outputStream.write(content.getBytes());
                PopUpDialogManager.successPopUp(getContext(), getString(R.string.salvataggio), getString(R.string.salvataggio_riuscito));
            }
        } catch (IOException e) {
            PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), getString(R.string.fallimento_salvataggio_file));
        }
    }

    // Intent per scegliere quale file aprire
    private void chooseFileToRead() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);  // Limita i risultati ai file "aperti"

        startForResultRead.launch(intent);
    }

    // Gestisco il contenuto del file letto
    private final ActivityResultLauncher<Intent> startForResultRead =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = Objects.requireNonNull(result.getData()).getData();
                    if (uri != null) {
                        // Leggi il contenuto del file
                        String fileContent = readFile(uri);
                        impostazioniViewModel.caricaCredenziali(fileContent);
                    }
                } else {
                    PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), getString(R.string.fallimento_selezione_file));
                }
            });

    // Leggo il contenuto del file
    private String readFile(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri)) {
            if (inputStream != null) {
                int character;
                while ((character = inputStream.read()) != -1) {
                    stringBuilder.append((char) character);  // Aggiungi i caratteri letti
                }
            }
        } catch (IOException e) {
            PopUpDialogManager.errorPopup(getContext(), getString(R.string.err), getString(R.string.fallimento_lettura_file));
        }
        return stringBuilder.toString();
    }
}