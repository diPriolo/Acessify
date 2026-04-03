package com.example.Acessify.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Local;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMapaProximos extends RecyclerView.Adapter<AdapterMapaProximos.MyViewHolder>
{
    private List<Local> locais;

    public AdapterMapaProximos(List<Local> locais) {
        this.locais = locais;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_mapa_proximo,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (locais.get(position).getNomeLocal().length()>16){
            holder.nome.setText(diminuirNomer(locais.get(position).getNomeLocal()));
        }else {
            holder.nome.setText(locais.get(position).getNomeLocal());
        }
        StorageReference ref = ConfiguraçaoFirebase.getStorage().child("locais").child(locais.get(position).getIdLocal()).child("fotos_oficiais/capa.png");
        ref.getDownloadUrl().addOnCompleteListener(uri ->{
            Log.d( "onBindViewHolder: ","uri pegado"+uri.toString());
            Picasso.get().load(uri.getResult()).centerCrop().resize(100,100).into(holder.capa);
        });
        holder.nivel.setImageResource(locais.get(position).resgatarIcon());
        holder.regiao.setText(locais.get(position).getZona());
    }


    @Override
    public int getItemCount() {
        return locais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView capa, nivel;
        private TextView nome,regiao;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            regiao = itemView.findViewById(R.id.regiom);
            capa = itemView.findViewById(R.id.capam);
            nivel = itemView.findViewById(R.id.nivelm);
            nome = itemView.findViewById(R.id.titulom);
        }
    }
    public  String  diminuirNomer(String nome){
        String[] quebra = nome.split("");
        String resul = "";
        for (int i = 0; i<16;i++){
            resul = resul +quebra[i];
        }
        resul = resul+"...";
        Log.d("TAG", "diminuirNomer:"+resul);
        return (resul);
    }
}
