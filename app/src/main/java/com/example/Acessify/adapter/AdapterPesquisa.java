package com.example.Acessify.adapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;
import com.example.Acessify.model.Local;

import java.util.ArrayList;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.ViewHolder> {
    public AdapterPesquisa(ArrayList<Local> locais) {
        this.locais = locais;
    }

    private ArrayList<Local> locais;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_pesquisa,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nome.setText(locais.get(position).getNomeLocal());
        holder.icon.setImageResource(locais.get(position).resgatarIcon());
    }

    @Override
    public int getItemCount() {
        return locais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView nome;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          icon =  itemView.findViewById(R.id.imageView14);
          nome = itemView.findViewById(R.id.textView17);
        }
    }
    public void limpar(){
        locais.clear();
        notifyDataSetChanged();
    }
}
