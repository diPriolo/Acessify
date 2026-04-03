package com.example.Acessify.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;

import java.util.ArrayList;
import java.util.List;

public class AdapterPerfilAval extends RecyclerView.Adapter<AdapterPerfilAval.MyViewHolder> {
    public AdapterPerfilAval() {
    }

    public AdapterPerfilAval(ArrayList<Pair<Avaliacao, Local>> avaliacaos) {
        this.avaliacaos = avaliacaos;
    }

    private ArrayList<Pair<Avaliacao, Local>> avaliacaos = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_perfil_avaliacao,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Avaliacao av = avaliacaos.get(position).first;
        Local l = avaliacaos.get(position).second;

        holder.nome.setText(l.getNomeLocal());
        holder.subNome.setText("-"+l.getSubNomeLocal());
        holder.nivel.setText(av.getNotaAvaliacao());
        holder.icon.setImageResource(av.recuperarIcon());
        holder.nivel.setTextColor(ContextCompat.getColor(holder.nivel.getContext(),  av.recuperarNivel().second));

    }

    @Override
    public int getItemCount() {
        return avaliacaos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome, subNome,nivel;
        ImageView icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeL);
            subNome = itemView.findViewById(R.id.subNome);
            nivel= itemView.findViewById(R.id.txnuvel);
            icon = itemView.findViewById(R.id.imageView16);
        }
    }
    public void atualizar(ArrayList<Pair<Avaliacao, Local>> lista){
            avaliacaos.clear();
            avaliacaos.addAll(lista);
            notifyDataSetChanged();

    }
}
