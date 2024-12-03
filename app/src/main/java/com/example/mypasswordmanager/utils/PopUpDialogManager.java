package com.example.mypasswordmanager.utils;

import android.app.Dialog;
import android.content.Context;

import com.example.mypasswordmanager.R;
import com.saadahmedev.popupdialog.PopupDialog;

public class PopUpDialogManager {

    public static void successPopUp(Context context, String header, String descrizione){

        PopupDialog.getInstance(context)
                .statusDialogBuilder()
                .createSuccessDialog()
                .setActionButtonText("OK!")
                .setBackgroundColor(R.color.dialogPopupBackground)
                .setDescriptionTextColor(R.color.dialogPopupTextcolor)
                .setHeading(header)
                .setDescription(descrizione)
                .build(Dialog::dismiss)
                .show();
    }


    public static void errorPopup(Context context, String header, String descrizione){
        PopupDialog.getInstance(context)
                .statusDialogBuilder()
                .createErrorDialog()
                .setBackgroundColor(R.color.dialogPopupBackground)
                .setDescriptionTextColor(R.color.dialogPopupTextcolor)
                .setActionButtonText("OK!")
                .setHeading(header)
                .setDescription(descrizione)
                .build(Dialog::dismiss)
                .show();
    }
}
