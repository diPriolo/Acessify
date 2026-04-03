package com.example.Acessify.adapter;

import android.content.ClipData;
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
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Local;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLocaisSalvos extends RecyclerView.Adapter<AdapterLocaisSalvos.MyViewHolder>
{
    private List<Local> locais;

    public AdapterLocaisSalvos(List<Local> locais) {
        this.locais = locais;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_mapa_salvos,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (locais.size() == 0){return;}

        if (locais.get(position).getNomeLocal().length()>14){
            holder.nome.setText(diminuirNomer(locais.get(position).getNomeLocal()));
        }else {
            holder.nome.setText(locais.get(position).getNomeLocal());
        }
        StorageReference ref = ConfiguraçaoFirebase.getStorage().child("locais").child(locais.get(position).getIdLocal()).child("fotos_oficiais/capa.png");
        ref.getDownloadUrl().addOnCompleteListener(uri ->{
            Log.d( "onBindViewHolder: ","uri pegado"+uri.toString());
            Picasso.get().load(uri.getResult()).centerCrop().resize(100,100).into(holder.capa);
            this.locais.get(position).setCapa(uri.getResult());
        });
        holder.nivel.setImageResource(locais.get(position).resgatarIcon());
        holder.regiao.setText(locais.get(position).getZona());
    }
    public interface ItemTouchHelperViewHolder {
        void onItemSelected();
        void onItemClear();
    }


    @Override
    public int getItemCount() {
        return locais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private ImageView capa, nivel;
        private TextView nome,regiao;


        public MyViewHolder(@NonNull View itemView) {


            super(itemView);
            regiao = itemView.findViewById(R.id.regiom);

            capa = itemView.findViewById(R.id.capam);
            nivel = itemView.findViewById(R.id.nivelm);
            nome = itemView.findViewById(R.id.titulom);
        }

        @Override
        public void onItemSelected() {
            // Quando o item está sendo arrastado
            itemView.setBackgroundColor(Color.TRANSPARENT); // remove borda/seleção
            itemView.setElevation(0); // remove a sombra padrão
        }

        @Override
        public void onItemClear() {
            // Quando o item solta
            itemView.setBackgroundColor(Color.TRANSPARENT);
            itemView.setElevation(0);
        }
    }
    public void atualizar(List<Local> l){
        Log.d("adp", "lista recebida: " + l.size());
        locais.clear();
        locais.addAll(l);
        Log.d("TAG", "atualzar: "+ locais.size());
        notifyDataSetChanged();

    }
    public  String  diminuirNomer(String nome){
        String[] quebra = nome.split("");
        String resul = "";
        for (int i = 0; i<14;i++){
            resul = resul +quebra[i];
        }
        resul = resul+"...";
        Log.d("TAG", "diminuirNomer:"+resul);
        return (resul);
    }

}
