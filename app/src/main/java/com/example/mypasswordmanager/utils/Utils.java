package com.example.mypasswordmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Utils {

    public static int getImgFromShared(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String imageName = sharedPreferences.getString("imageName", "my_logo");
        // Recupera l'ID della risorsa
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static  void saveImageNameToSharedPreferences(Context context, String imageName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageName", imageName);
        editor.apply();

        // Mostra un messaggio di conferma
        Toast.makeText(context, "Immagine salvata: " + imageName, Toast.LENGTH_SHORT).show();
    }
}
