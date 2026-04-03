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

public class AdapterImgAvaliacao extends RecyclerView.Adapter<AdapterImgAvaliacao.ViewHolder> {
    private List<Uri> listaImg;

    public AdapterImgAvaliacao(List<Uri> listaImg) {
        this.listaImg = listaImg;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_img_avaliacao,parent,false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(listaImg.get(position)).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aumentar imagem e pausar o carrossel;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View viw = inflater.inflate(R.layout.alert_carrosel_sheet,null);


                builder.setView(viw);
                //colocar img
                ImageView imageView = (ImageView) viw.findViewById(R.id.alertCarroselImg);

                Picasso.get().load(listaImg.get(holder.getAdapterPosition())).into(imageView);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgAvaliacao);

        }
    }
}
