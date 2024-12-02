package com.example.mypasswordmanager.entita;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "credenziali")
public class Credenziali implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id; // Chiave primaria

    private String servizio; // Nome del servizio
    private String username; // username associato
    private String password; // Password associata

    public Credenziali() {}

    // Costruttore
    public Credenziali(String service, String username, String password) {
        this.servizio = service;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServizio() {
        return servizio;
    }

    public void setServizio(String servizio) {
        this.servizio = servizio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credenziali{" +
                "id=" + id +
                ", servizio='" + servizio + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
