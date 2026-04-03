package com.example.Acessify.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;

import java.util.List;


public class AdapterGaleriaSheet extends RecyclerView.Adapter<AdapterGaleriaSheet.ViewHolder> {
    public AdapterGaleriaSheet(List<String[]> nivelInfra) {
        this.nivelInfra = nivelInfra;
    }

    private List<String[]> nivelInfra;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.galeria_adapter,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String[] nivel = nivelInfra.get(position);
    holder.titulo.setTextColor(Color.parseColor(nivel[0]));
      holder.texto.setTextColor(Color.parseColor(nivel[0]));
       holder.titulo.setText(nivel[1]);
       holder.texto.setText(nivel[2]);
      holder.icon.setImageResource(Integer.parseInt(nivel[3]));
        Log.d("onBindViewHolder: ",""+nivelInfra.size());

    }

    @Override
    public int getItemCount() {
        return nivelInfra.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo;
        private TextView texto;
        private ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.galeriaTitulo);
            texto = itemView.findViewById(R.id.textoGaleria);
            icon = itemView.findViewById(R.id.iconGaleria);
        }
    }
}
