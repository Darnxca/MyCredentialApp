package com.example.mypasswordmanager.utils.dialog;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.utils.PassphraseCallback;
import com.example.mypasswordmanager.utils.Utils;

public class MyCustomDialogImage {

    private static View lastSelectedView = null;

    public static void showImageSelectorDialog(Context context) {
        // Crea il dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_image_selector, null);
        builder.setView(dialogView);

        // Trova il contenitore delle immagini
        LinearLayout imageContainer = dialogView.findViewById(R.id.image_container);

        // Aggiungi immagini dinamicamente
        int[] imageResIds = {R.drawable.my_logo, R.drawable.true_logo}; // Le tue immagini
        String[] imageNames = {"my_logo", "true_logo"}; // Nomi corrispondenti

        for (int i = 0; i < imageResIds.length; i++) {
            ImageView imageView = new ImageView(context);
            // Imposta le dimensioni a 100x100 dp
            int sizeInDp = (int) (100 * context.getResources().getDisplayMetrics().density); // Conversione da dp a px
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeInDp, sizeInDp);
            params.setMargins(16, 16, 16, 16); // Margini intorno all'immagine
            imageView.setLayoutParams(params);

            imageView.setImageResource(imageResIds[i]);
            imageView.setPadding(16, 16, 16, 16);
            imageView.setTag(imageNames[i]); // Salva il nome nel tag

            // Imposta lo sfondo predefinito per il click
            imageView.setBackgroundResource(R.drawable.image_selector_background);

            // Imposta il click listener
            imageView.setOnClickListener(v -> {

                // Ripristina lo sfondo dell'ultimo selezionato
                if (lastSelectedView != null) {
                    lastSelectedView.setBackgroundResource(R.drawable.image_selector_background);
                }
                // Aggiorna lo sfondo dell'immagine cliccata
                imageView.setBackgroundResource(R.drawable.image_selected_background);
                lastSelectedView = imageView;

                Utils.saveImageNameToSharedPreferences(context, v.getTag().toString());

            });

            // Aggiungi l'immagine al contenitore
            imageContainer.addView(imageView);
        }

        // Mostra il dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}




