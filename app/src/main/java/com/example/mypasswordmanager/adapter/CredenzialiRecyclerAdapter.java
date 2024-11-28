package com.example.mypasswordmanager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypasswordmanager.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CredenzialiRecyclerAdapter extends RecyclerView.Adapter<CredenzialiRecyclerAdapter.ViewHolder> {

    private List<String[]> data;

    public CredenzialiRecyclerAdapter(List<String[]> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_credenziali, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String[] item = data.get(position);

        // prova
        final int[] i = {0};
        holder.text.forEach( (element) -> {
            element.setText(item[i[0]]);
            i[0]++;

        });


        // Recupera il relativo layout dell'elemento
        RelativeLayout itemLayout = (RelativeLayout) holder.itemView;
        itemLayout.setOnClickListener(view -> {
            Toast.makeText(itemLayout.getContext(), "Mammt"+item[0], Toast.LENGTH_LONG).show();
        });

        itemLayout.setOnLongClickListener(view -> {
            Toast.makeText(itemLayout.getContext(), item[0], Toast.LENGTH_LONG).show();
            return true;
        });





    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String[]> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ArrayList<TextView> text = new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text.add(itemView.findViewById(R.id.id));
            text.add(itemView.findViewById(R.id.nome_servizio));
            text.add(itemView.findViewById(R.id.username));
            text.add(itemView.findViewById(R.id.password));
            text.add(itemView.findViewById(R.id.val_password));
        }
    }
}
