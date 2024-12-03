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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


        RelativeLayout relativeLayout = customView.findViewById(R.id.parent_layout);
        LinearLayout linearLayout = customView.findViewById(R.id.center_layout);
        ImageButton copia = customView.findViewById(R.id.copy);
        ImageButton edit = customView.findViewById(R.id.edit);
        ImageButton close = customView.findViewById(R.id.center_close);
        ImageButton copyAll = customView.findViewById(R.id.copyAll);
        ImageButton remove = customView.findViewById(R.id.remove);


        MySecuritySystem mySecuritySystem = null;
        try {
            mySecuritySystem = new MySecuritySystem();

        } catch (Exception e) {
            PopUpDialogManager.errorPopup(context, String.valueOf(R.string.err), String.valueOf(R.string.errore));
        }
        MySecuritySystem finalMySecuritySystem = mySecuritySystem;

        // Impostare il layout personalizzato nel dialogo
        builder.setView(customView);
        // Creare il dialogo
        AlertDialog dialog = builder.create();

        relativeLayout.setOnClickListener(v -> dialog.dismiss());

        linearLayout.setOnClickListener(v -> dialog.dismiss());

        copia.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = null;
            try {
                clip = ClipData.newPlainText(R.string.cpy + "", Objects.requireNonNull(finalMySecuritySystem).decrypt(credenziali.getPassword()));
            } catch (Exception e) {
                PopUpDialogManager.errorPopup(context, context.getString(R.string.err), context.getString(R.string.erroreDecriptazione));
            }
            clipboard.setPrimaryClip(Objects.requireNonNull(clip));

            dialog.dismiss();

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
            ClipData clip = null;
            try {
                clip = ClipData.newPlainText(R.string.cpyUP +"",
                        Objects.requireNonNull(finalMySecuritySystem).decrypt(credenziali.getUsername())+ " " +
                             finalMySecuritySystem.decrypt(credenziali.getPassword()));
            } catch (Exception e) {
                PopUpDialogManager.errorPopup(context, context.getString(R.string.err), context.getString(R.string.erroreDecriptazione));
            }
            clipboard.setPrimaryClip(Objects.requireNonNull(clip));

            dialog.dismiss();
        });

        remove.setOnClickListener( v -> {
            // Sposta l'operazione su un thread separato
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(context);

                database.credenzialiDao().deleteCredenziali(credenziali);

                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    PopUpDialogManager.successPopUp(context, context.getString(R.string.cancellazione),context.getString(R.string.cancellata));
                    dialog.dismiss();
                    data.remove(position);
                    credenzialiRecyclerAdapter.notifyDataSetChanged();
                });


            });
        });

        // Mostrare il dialog
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

    }

}
