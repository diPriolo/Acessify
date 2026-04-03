package com.example.Acessify.adapter;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;
import com.example.Acessify.avaliacao.FragmentAvaliacao2;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCreateAvaliacao extends RecyclerView.Adapter<AdapterCreateAvaliacao.ViewHolder> {
    private List<Uri> listaImg;
    private FragmentAvaliacao2 frag;

    public AdapterCreateAvaliacao(List<Uri> listaImg, FragmentAvaliacao2 frag) {
        this.listaImg = listaImg;
        this.frag = frag;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.criar_avaliacao_recycler,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(listaImg.get(position)).resize(500,500).centerCrop().into(holder.img);
        holder.fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaImg.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                frag.atualizaLista(listaImg);

            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aumentar imagem
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View viw = inflater.inflate(R.layout.alert_carrosel_sheet,null);


                builder.setView(viw);
                //colocar img
                ImageView imageView = (ImageView) viw.findViewById(R.id.alertCarroselImg);

                Picasso.get().load(listaImg.get(holder.getAdapterPosition())).resize(800,800).centerInside().into(imageView);
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaImg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView img;
        private ImageView fecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgcriarAvaliacao);
            fecha = itemView.findViewById(R.id.imageView13);
        }
    }
  public List<Uri> atualizarLista(){
        return listaImg;
  }
}
