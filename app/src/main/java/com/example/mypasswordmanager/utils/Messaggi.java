package com.example.mypasswordmanager.utils;

public enum Messaggi {
    SALVATAGGIO_RIUSCITO("Dati salvati con successo!"),
    CAMPO_NOME_SERVIZIO_VUOTO("Il campo nome servizio è vuoto"),
    CAMPO_USERNAME_VUOTO("Il campo username è vuoto"),
    CAMPO_PASSWORD_VUOTO("Il campo password è vuoto");


    private final String messaggio;

    // Costruttore per associare il messaggio ad ogni costante
    Messaggi(String messaggio) {
        this.messaggio = messaggio;
    }

    // Metodo per ottenere il messaggio associato alla costante
    public String getMessaggio() {
        return this.messaggio;
    }
}
