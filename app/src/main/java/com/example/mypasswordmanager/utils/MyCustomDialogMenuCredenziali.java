package com.example.mypasswordmanager.utils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.adapter.CredenzialiRecyclerAdapter;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyCustomDialogMenuCredenziali {

    @SuppressLint("NotifyDataSetChanged")
    public static void showCustomDialog(Context context, Fragment fragment, Credenziali credenziali, List<Credenziali> data, int position, CredenzialiRecyclerAdapter credenzialiRecyclerAdapter) {
        // Crea il builder per l'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflazionare il layout personalizzato
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog_menu_credenziali, null);

        ImageButton copia = customView.findViewById(R.id.copy);
        ImageButton edit = customView.findViewById(R.id.edit);
        ImageButton close = customView.findViewById(R.id.center_close);
        ImageButton copyAll = customView.findViewById(R.id.copyAll);
        ImageButton remove = customView.findViewById(R.id.remove);

        // Impostare il layout personalizzato nel dialogo
        builder.setView(customView);
        // Creare il dialogo
        AlertDialog dialog = builder.create();

        copia.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("carmiaine", credenziali.getPassword());
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
            Toast.makeText(context, "Mammt"+credenziali, Toast.LENGTH_LONG).show();
        });

        edit.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("credenziali", credenziali);

            NavController navController = NavHostFragment.findNavController(fragment);
            // Aggiungere sempre il nuovo fragment a mobile navigation
            navController.navigate(R.id.navigation_aggiorna_credenziali, bundle);
            dialog.dismiss();
        });

        close.setOnClickListener( v -> dialog.dismiss());

        copyAll.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("carmiaine", credenziali.getUsername() + " "+credenziali.getPassword());
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
            Toast.makeText(context, "Mammt"+credenziali, Toast.LENGTH_LONG).show();
        });

        remove.setOnClickListener( v -> {
            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(context);

                database.credenzialiDao().deleteCredenziali(credenziali);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    Toast.makeText(context, "Credenziale cancellata", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    data.remove(position);
                    credenzialiRecyclerAdapter.notifyDataSetChanged();
                });


            });
        });


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        // Mostrare il dialog
        dialog.show();
    }

}
