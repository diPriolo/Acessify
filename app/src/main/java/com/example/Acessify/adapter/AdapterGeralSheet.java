package com.example.Acessify.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;

import java.util.List;


public class AdapterGeralSheet extends RecyclerView.Adapter<AdapterGeralSheet.MyViewHolder> {
    private List<Pair<String,Integer>> niveis;
    public AdapterGeralSheet(List<Pair<String,Integer>> niveis) {
        this.niveis = niveis;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.geral_sheet_recycler,parent,false);
        return new MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(niveis.get(position).first);
        holder.icon.setImageResource(niveis.get(position).second);

    }

    @Override
    public int getItemCount() {
        return niveis.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconAcessibilidade);
            text = itemView.findViewById(R.id.nomeAcessibilidade);
        }

    }
}
