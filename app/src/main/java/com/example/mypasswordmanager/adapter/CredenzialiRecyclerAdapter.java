package com.example.mypasswordmanager.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypasswordmanager.R;
import com.example.mypasswordmanager.entita.Credenziali;
import com.example.mypasswordmanager.utils.MyCustomDialogMenuCredenziali;

import java.util.ArrayList;
import java.util.List;

public class CredenzialiRecyclerAdapter extends RecyclerView.Adapter<CredenzialiRecyclerAdapter.ViewHolder> {

    private List<Credenziali> data;
    private Context activityContext;
    private Fragment fragment;

    public CredenzialiRecyclerAdapter(List<Credenziali> data, Context context, Fragment fragment) {
        this.data = data;
        this.activityContext = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_credenziali, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Credenziali credenziali = data.get(position);

        holder.text.get(0).setText(credenziali.getId() + ""); // setto id all'elemento
        holder.text.get(1).setText(credenziali.getServizio()+": "); // setto nome del servizio
        holder.text.get(2).setText(credenziali.getUsername()); // setto l'username
        holder.text.get(4).setText(credenziali.getPassword()); // setto la password

        // Recupera il relativo layout dell'elemento
        RelativeLayout itemLayout = (RelativeLayout) holder.itemView;

        itemLayout.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) activityContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("carmiaine", credenziali.getPassword());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(itemLayout.getContext(), "Mammt"+credenziali, Toast.LENGTH_LONG).show();
        });

        itemLayout.setOnLongClickListener(view -> {
            Context context = view.getContext();

            MyCustomDialogMenuCredenziali.showCustomDialog(context, fragment, credenziali, data, position, this);
            return true;

        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Credenziali> newData) {
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
