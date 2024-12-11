package com.example.mypasswordmanager.ui.impostazioni;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.database.AppDatabase;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.mykeystore.MySecuritySystem;
import com.example.mypasswordmanager.utils.MyCustomDialogPassphrase;
import com.example.mypasswordmanager.utils.MyCypher;
import com.example.mypasswordmanager.utils.PassphraseCallback;
import com.example.mypasswordmanager.utils.PopUpDialogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class impostazioniViewModel extends AndroidViewModel{

    private final Context context;
    private final MutableLiveData<Boolean> isChecked;
    private final MutableLiveData<String> stato;
    private final MutableLiveData<String> encrypt;
    private String passphrase = "";


    public impostazioniViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.isChecked = new MutableLiveData<>();
        this.isChecked.setValue(isDarkModeEnabled());
        this.stato = new MutableLiveData<>();
        this.encrypt = new MutableLiveData<>();
    }

    public LiveData<String> isData() {
        return stato;
    }

    public LiveData<Boolean> isChecked() {
        return isChecked;
    }

    public void setPassphrase(String passphrase){ this.passphrase = passphrase;}
    public String getPassphrase(){return passphrase;}

    public void changeTheme(CompoundButton buttonView, boolean isChecked){
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            this.isChecked.setValue(true);
            saveThemePreference(true);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            this.isChecked.setValue(false);
            saveThemePreference(false);
        }
    }

    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkMode", isDarkMode);
        editor.apply();
    }

    private boolean isDarkModeEnabled() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isDarkMode", true);
    }

    public void resetDataMessage() {
        stato.setValue(null);
    }

    public LiveData<String> getEncrypt() {return encrypt;}

    // recupero dati dal db
    private CompletableFuture<List<Credenziali>> getCredenzialiFromDB() {
        return CompletableFuture.supplyAsync(() -> {
            // Ottieni l'istanza del database
            AppDatabase database = AppDatabase.getInstance(this.context);

            // Recupera le credenziali dal database
            return database.credenzialiDao().getAllCredenziali();
        });
    }

    private CompletableFuture<Boolean> deleteCredenziali(){
        return CompletableFuture.supplyAsync(() -> {
            // Ottieni l'istanza del database
            AppDatabase database = AppDatabase.getInstance(this.context);

            // Recupera le credenziali dal database
            database.credenzialiDao().deleteAllCredenziali();
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void scaricaCredenziali() {
        getCredenzialiFromDB().thenAccept(data -> {
            // Esegui operazioni con i dati nel thread principale
            new Handler(Looper.getMainLooper()).post(() -> {

                try {
                    MySecuritySystem mySecuritySystem = MySecuritySystem.getInstance();

                    Credenziali ultimaCredenziale = data.get(data.size() - 1);

                    StringBuilder jsonDB = new StringBuilder();
                    jsonDB.append("[");

                    data.forEach(credenziale -> {

                        try {
                            jsonDB.append("{")
                                    .append("\"servizio\":\"").append(mySecuritySystem.decrypt(credenziale.getServizio())).append("\",")
                                    .append("\"username\":\"").append(mySecuritySystem.decrypt(credenziale.getUsername())).append("\",")
                                    .append("\"password\":\"").append(mySecuritySystem.decrypt(credenziale.getPassword())).append("\"")
                                    .append("}");

                        } catch (Exception e) {
                            stato.setValue(this.context.getString(R.string.erroreDecriptazione)+ ";err");
                        }

                        if(!credenziale.equals(ultimaCredenziale))
                            jsonDB.append(",");
                    });
                    jsonDB.append("]");



                    Log.d("data",jsonDB.toString());
                    Log.d("data",this.passphrase);

                    String encryptDB = MyCypher.encrypt(jsonDB.toString(), this.passphrase);

                    Log.d("data",encryptDB);
                    encrypt.setValue(encryptDB);


                } catch (Exception e) {
                    stato.setValue(this.context.getString(R.string.errore)+ ";err");
                }
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void caricaCredenziali(String encryptdata){

        String plaindata;
        try {
            plaindata = MyCypher.decrypt(encryptdata, this.passphrase);

            final MySecuritySystem[] mySecuritySystem = {null};
            Executor executor = Executors.newSingleThreadExecutor();

            deleteCredenziali().thenAccept(data -> {
                try {
                    mySecuritySystem[0] = MySecuritySystem.getInstance();
                    JSONArray jsonArray = new JSONArray(plaindata);

                    // Iterare attraverso gli elementi dell'array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        // Leggere i valori
                        try {
                            String servizio_cypher = mySecuritySystem[0].encrypt(obj.getString("servizio"));
                            String username_cypher = mySecuritySystem[0].encrypt(obj.getString("username"));
                            String password_cypher = mySecuritySystem[0].encrypt(obj.getString("password"));


                            executor.execute(() -> {
                                AppDatabase database = AppDatabase.getInstance(this.context);
                                Credenziali credenziali = new Credenziali(servizio_cypher, username_cypher, password_cypher);
                                database.credenzialiDao().insertCredenziali(credenziali);
                            });


                        } catch (Exception e) {
                            // Aggiorna LiveData sul main thread
                            new Handler(Looper.getMainLooper()).post(() ->
                                    stato.setValue(this.context.getString(R.string.erroreCifratura) + ";err")
                            );
                        }
                    }

                    // Aggiorna LiveData sul main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            stato.setValue(this.context.getString(R.string.credenziali_caricate))
                    );
                } catch (Exception e) {
                    // Aggiorna LiveData sul main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            stato.setValue(this.context.getString(R.string.errore) + ";err")
                    );
                }
            });

        } catch (Exception e) {
            stato.setValue(this.context.getString(R.string.erroreDecriptazione)+ ";err");
        }

    }

}