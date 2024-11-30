package com.example.mypasswordmanager.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mypasswordmanager.R;

import java.util.Objects;

public class MyCustomDialogMenuCredenziali {

    public static void showCustomDialog(Context context) {
        // Crea il builder per l'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

// Impostare un titolo per il dialogo
        builder.setTitle("Azione Personalizzata");

// Impostare un'icona per il dialogo (opzionale)
        builder.setIcon(R.drawable.my_logo); // Sostituisci con la tua icona

// Inflazionare il layout personalizzato
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog_menu_credenziali, null);
/*
// Ottenere il riferimento ai controlli del layout
        TextView messageTextView = customView.findViewById(R.id.custom_message);
        Button actionButton = customView.findViewById(R.id.custom_button);

// Modificare il testo del TextView (opzionale)
        messageTextView.setText("Messaggio Personalizzato da Java!");

// Definire cosa succede quando si clicca sul pulsante
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azione da eseguire quando si clicca il pulsante
                // Ad esempio, mostrare un messaggio
                messageTextView.setText("Azione Eseguita!");
            }


        });
*/
    // Impostare il layout personalizzato nel dialogo
        builder.setView(customView);

    // Aggiungere i pulsanti del dialogo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Azione quando si preme OK
                dialog.dismiss();  // Chiudi il dialogo
            }


        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Azione quando si preme Annulla
                dialog.dismiss();  // Chiudi il dialogo
            }
        });

        // Creare il dialogo
        AlertDialog dialog = builder.create();


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        // Mostrare il dialogo
        dialog.show();

    }

}
