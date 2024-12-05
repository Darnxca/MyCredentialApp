package com.example.mypasswordmanager.ui.impostazioni;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class impostazioniViewModel extends AndroidViewModel {

    private final Context context;
    private final MutableLiveData<Boolean> isChecked;

    public impostazioniViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.isChecked = new MutableLiveData<>();
        this.isChecked.setValue(isDarkModeEnabled());
    }

    public LiveData<Boolean> isChecked() {
        return isChecked;
    }

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

}