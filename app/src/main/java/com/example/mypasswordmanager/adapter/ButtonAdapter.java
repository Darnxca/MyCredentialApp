package com.example.mypasswordmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypasswordmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    private final Context context;
    private final List<String> categories;


    // Costruttore dell'adapter
    public ButtonAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = new ArrayList<>(categories); // Copia la lista per evitare problemi
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usa ContextThemeWrapper per applicare uno stile personalizzato ai bottoni
        ContextThemeWrapper themedContext = new ContextThemeWrapper(context, R.style.myButton);
        Button button = new Button(themedContext);

        // Configura i parametri di layout del bottone
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16); // Imposta i margini
        button.setLayoutParams(params);

        return new ButtonViewHolder(button);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        String category = categories.get(position);
        holder.button.setText(category); // Imposta il testo del bottone
    }

    @Override
    public int getItemCount() {
        return categories.size(); // Restituisce il numero di categorie
    }

    // Metodo per aggiornare dinamicamente i dati
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String> newCategories) {
        categories.clear(); // Pulisce la lista esistente
        categories.addAll(newCategories); // Aggiunge i nuovi dati
        notifyDataSetChanged(); // Notifica i cambiamenti alla RecyclerView
    }

    // ViewHolder per contenere i bottoni
    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ButtonViewHolder(@NonNull Button itemView) {
            super(itemView);
            button = itemView; // Collega il bottone al ViewHolder
        }
    }
}
