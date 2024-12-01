package com.example.mypasswordmanager.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.ui.aggiornaCredenziali.AggiornaFragment;

import java.util.Objects;

public class MyCustomDialogMenuCredenziali {

    public static void showCustomDialog(Context context, Fragment fragment) {
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

        edit.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(fragment);
            // ricorda di aggiungere sempre il nuovo fragment a mobile navigation
            navController.navigate(R.id.navigation_aggiorna_credenziali);
            dialog.dismiss();
        });
        close.setOnClickListener( v -> dialog.dismiss());

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        // Mostrare il dialogo
        dialog.show();

    }

}
