package com.example.mypasswordmanager.utils.dialog;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.utils.PopUpDialogManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.OutputStream;

public class MyCustomDialogQrcode {
    private static Bitmap generatedBitmap;

    public static void showQRCodeDialog(Context context, String text){
        try {
            // Genera il QR Code
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            generatedBitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 800, 800);

            // Crea il Dialog
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_qrcode, null);

            ImageView qrCodeImageView = dialogView.findViewById(R.id.dialog_qr_code_image);
            qrCodeImageView.setImageBitmap(generatedBitmap);

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create();

            // Aggiungi listener per il pulsante "Salva QR Code"
            Button btnSaveQR = dialogView.findViewById(R.id.dialog_btn_save_qr);
            btnSaveQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveQRCodeToGallery(context, generatedBitmap);
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (WriterException e) {
            PopUpDialogManager.errorPopup(context, context.getString(R.string.err), context.getString(R.string.errore_generazione));
        }
    }

    private static void saveQRCodeToGallery(Context context, Bitmap bitmap) {
        try {
            // Imposta i metadati per il file da salvare
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "qr_code_" + System.currentTimeMillis() + ".png");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/QRCodeApp");

            // Ottieni il ContentResolver per salvare il file
            ContentResolver resolver = context.getContentResolver();
            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (uri != null) {
                // Scrivi l'immagine nel file
                OutputStream outputStream = resolver.openOutputStream(uri);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                    PopUpDialogManager.successPopUp(context, context.getString(R.string.qrcode_generato), context.getString(R.string.qrcode_salvato));
                }
            } else {
                PopUpDialogManager.errorPopup(context, context.getString(R.string.err), context.getString(R.string.errore_salvataggio_qrcode));
            }
        } catch (Exception e) {
            PopUpDialogManager.errorPopup(context, context.getString(R.string.err), context.getString(R.string.errore_salvataggio_qrcode));
        }
    }
}
