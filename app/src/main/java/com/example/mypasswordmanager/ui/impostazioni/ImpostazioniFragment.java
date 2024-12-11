package com.example.mypasswordmanager.ui.impostazioni;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.example.mypasswordmanager.utils.MyCustomDialogPassphrase;
import com.example.mypasswordmanager.utils.PassphraseCallback;
import com.example.mypasswordmanager.utils.PopUpDialogManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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


    // Avvia l'Intent per far scegliere all'utente il percorso
    private void chooseFileLocation() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "example.txt");  // Imposta un nome di file predefinito

        // Usa ActivityResultLauncher per gestire il risultato
        startForResult.launch(intent);
    }

    // Usa il nuovo sistema per gestire i risultati delle Activity
    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            writeToFile(uri, encryptData);
                        }
                    } else {
                        Log.e(TAG, "File selection was canceled or failed");
                    }
                }
            });

    // Scrivere il contenuto nel file selezionato
    private void writeToFile(Uri uri, String content) {
        try (OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                outputStream.write(content.getBytes());
                Toast.makeText(requireActivity(), "File saved successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error writing file", e);
            Toast.makeText(requireActivity(), "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }


    private void chooseFileToRead() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");  // Tipo di file che si desidera leggere (ad esempio, file di testo)
        intent.addCategory(Intent.CATEGORY_OPENABLE);  // Limita i risultati ai file "aperti"

        // Usa ActivityResultLauncher per gestire il risultato
        startForResultRead.launch(intent);
    }

    // Gestisci la selezione del file e leggi il contenuto
    private final ActivityResultLauncher<Intent> startForResultRead =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();  // Ottieni l'URI del file
                    if (uri != null) {
                        // Leggi il contenuto del file
                        String fileContent = readFile(uri);

                        impostazioniViewModel.caricaCredenziali(fileContent);

                    }
                } else {
                    Toast.makeText(requireActivity(), "File selection was canceled or failed", Toast.LENGTH_SHORT).show();
                }
            });

    // Leggere il contenuto del file
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
            Toast.makeText(requireActivity(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return stringBuilder.toString();  // Restituisci il contenuto del file come stringa
    }
}