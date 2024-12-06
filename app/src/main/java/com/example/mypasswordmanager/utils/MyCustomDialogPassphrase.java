package com.example.mypasswordmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import com.example.mypasswordmanager.R;

public class MyCustomDialogPassphrase {

    public static void openPassphraseDialog(Context context,PassphraseCallback callback) {
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.passphrase))
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Recupera la passphrase e chiamare il callback
                    String passphrase = input.getText().toString();
                    callback.onPassphraseEntered(passphrase);
                })
                .show();
    }
}
