package com.example.mypasswordmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.splashscreen.SplashScreen;

import com.example.mypasswordmanager.utils.PopUpDialogManager;

import java.util.concurrent.Executor;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> true );

        // Verifica se la biometria è disponibile sul dispositivo
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // La biometria è disponibile
                authenticateUser();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                PopUpDialogManager.errorPopup(this, getString(R.string.err), this.getString(R.string.biometric_no_hardware));
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                PopUpDialogManager.errorPopup(this, getString(R.string.err), this.getString(R.string.biometric_unavailable));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                PopUpDialogManager.errorPopup(this, getString(R.string.err), this.getString(R.string.biometric_non_enrolled));
                break;
        }
    }

    private void applySavedTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", true);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void authenticateUser() {
        // Esegui l'autenticazione biometrica o di sistema
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                goToMainActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Autenticazione fallita
                PopUpDialogManager.errorPopup(SplashActivity.this, getString(R.string.err), SplashActivity.this.getString(R.string.autenticazione_fallita));
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Errore di autenticazione
                PopUpDialogManager.errorPopup(SplashActivity.this, getString(R.string.err), errString.toString());
            }
        });

        // Configura il prompt di autenticazione
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(this.getString(R.string.autenticazione))
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        // Mostra il prompt di autenticazione
        biometricPrompt.authenticate(promptInfo);
    }

    private void goToMainActivity() {
        Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish(); // Chiudi l'activity corrente
    }


}