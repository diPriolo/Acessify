package com.example.Acessify.adapter;


import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterDuvidas extends RecyclerView.Adapter<AdapterDuvidas.MyViewHolder> {
    private List<String[]> duvidas;
    public AdapterDuvidas() {

       this.duvidas = lista();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_duvidas,parent,false);
        return new MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titulo.setText(lista().get(position)[0]); // recupera o testo e titulo no array
        holder.text.setText(lista().get(position)[1]);
        //click no botao
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.text.getVisibility() == View.GONE){
                    Animation roda = AnimationUtils.loadAnimation( holder.icon.getContext(), R.anim.abrir_duvida);
                    Animation dessc = AnimationUtils.loadAnimation( holder.icon.getContext(), R.anim.descer);
                    holder.icon.startAnimation(roda);
                    holder.text.startAnimation(dessc);
                    holder.text.setVisibility(View.VISIBLE);
                }else {

                    Animation roda = AnimationUtils.loadAnimation( holder.icon.getContext(), R.anim.fechar_duvido);
                    holder.text.setVisibility(View.GONE);


                    holder.icon.startAnimation(roda);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return lista().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView titulo;
        private TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icDuvida1);
            text = itemView.findViewById(R.id.txDuvida1);
            titulo = itemView.findViewById(R.id.textView25);

        }

    }
    public List<String[]> lista(){
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{
                "Quem somos",
                "A equipe Acessify se formou a partir de um projeto de TCC (Trabalho de Conclusão de Curso), com o objetivo de contribuir ao acesso à informação de pessoas com mobilidade inclusiva, promovendo um maior proveito do turismo na cidade de São Paulo."
        });

        list.add(new String[]{
                "Contato",
                "Você pode entrar em contato conosco através do nosso Instagram e nosso e-mail!\nEmail: acessifytcc@gmail.com\nInstagram: acessify_"
        });

        list.add(new String[]{
                "Iremos sempre atualizar o projeto?",
                "Nossos planos futuros envolvem dar continuidade ao projeto por meio das contribuições dos usuários e nossa própria manutenção periódica."
        });

        list.add(new String[]{
                "As informações são confiáveis?",
                "Sim! As informações serão adicionadas pelos administradores, possuirão data e sinalizarão quando estiverem desatualizadas, além das próprias avaliações de outros usuários, que contribuirão para uma maior confiabilidade das informações."
        });

        list.add(new String[]{
                "Como adicionar locais?",
                "Esse recurso será disponibilizado em breve. Pretendemos incrementar diversas novas ferramentas em futuras atualizações."
        });
        return list;
    }
    public void animar(TextView txt, Button btn){


    }
}

