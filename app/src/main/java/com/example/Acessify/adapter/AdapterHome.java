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

import java.util.ArrayList;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder>
{
    private List<Local> locais;

    public AdapterHome(List<Local> locais) {
        this.locais = locais;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_menu,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //nome
        if (locais.get(position).getNomeLocal().length()>12){
            holder.nome.setText(diminuirNomer(locais.get(position).getNomeLocal()));
        }else {
            holder.nome.setText(locais.get(position).getNomeLocal());
        }

        StorageReference ref = ConfiguraçaoFirebase.getStorage().child("locais").child(locais.get(position).getIdLocal()).child("fotos_oficiais/capa.png");
        ref.getDownloadUrl().addOnCompleteListener(uri ->{
            Log.d( "onBindViewHolder: ","uri pegado"+uri.toString());
            Picasso.get().load(uri.getResult()).into(holder.capa);
            this.locais.get(position).setCapa(uri.getResult());
        });
    }


    @Override
    public int getItemCount() {
        return locais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView capa, nivel,iconSalvo;
        private TextView nome;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            capa = itemView.findViewById(R.id.img_capa);
            nivel = itemView.findViewById(R.id.nivel_acessibilidade);
            iconSalvo = itemView.findViewById(R.id.icon_salvo);
            nome = itemView.findViewById(R.id.text_nome);
        }
    }
    public  String  diminuirNomer(String nome){
        String[] quebra = nome.split("");
        String resul = "";
        for (int i = 0; i<10;i++){
           resul = resul +quebra[i];
        }
        resul = resul+"...";
        Log.d("TAG", "diminuirNomer:"+resul);
    return (resul);
    }

}
