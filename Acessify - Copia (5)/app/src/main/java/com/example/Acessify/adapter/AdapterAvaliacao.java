package com.example.Acessify.adapter;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.Callback.ImagenCallback;
import com.example.Acessify.R;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class AdapterAvaliacao extends RecyclerView.Adapter<AdapterAvaliacao.ViewHolder> {
    public AdapterAvaliacao(List<Avaliacao> avaliacaos) {
        this.avaliacaos = avaliacaos;
    }

    private List<Avaliacao> avaliacaos;
    private View item;
    Animation scaleAnim;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.avaliacao_recycler, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final boolean[] like = {false};
        //textos:
        //nome
        Avaliacao avaliacao = avaliacaos.get(position);
        ConfiguraçaoFirebase.usuarioRef().child(avaliacao.getIdUsuario()).child("nomeUsuario").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                }
                holder.nome.setText(task.getResult().getValue(String.class));
            }
        });
        //n de avaliacoes
        ConfiguraçaoFirebase.usuarioRef().child(avaliacao.getIdUsuario()).child("avaliacoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 1) {
                    holder.numero.setText("Primeira Avaliação");
                } else holder.numero.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Recycler Avaliacao", "n avaliacoes: erro ");
            }
        });
        holder.nivel.setText(avaliacao.getNotaAvaliacao());
        Log.d("NIVEL", avaliacao.getNotaAvaliacao());
        holder.data.setText(avaliacao.getDataAvaliacao());
        holder.texto.setText(avaliacao.getTextoAvaliacao());
        holder.condicao.setText(avaliacao.getCondicaoAvaliacao());
        holder.ncurtidas.setText(String.valueOf(avaliacao.getCurtidasAvaliacao()));
        //icon e color
        Pair<Integer, Integer> pair = avaliacao.recuperarNivel();
        holder.nivel.setTextColor(ContextCompat.getColor(holder.nivel.getContext(), pair.second));
        holder.pin.setImageResource(pair.first);
        //imagens
        avaliacao.recuperarFotos(new ImagenCallback() {
            @Override
            public void onImagensCarregadas(List<Uri> imgs) {
                Log.d("Recycler Avaliacao", "recuperar imagens tamanho: " + imgs.size());
                if (imgs.size() > 0) {
                    AdapterImgAvaliacao imgAvaliacao = new AdapterImgAvaliacao(imgs);
                    holder.recyclerView.setAdapter(imgAvaliacao);
                    holder.recyclerView.setLayoutManager(new GridLayoutManager(item.getContext(), 3));
                }
            }
        });
        //likes
        //recuperar se ja foi curtido
        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("curtidas").equalTo(avaliacao.getIdAvaliacao()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d("recuperar like", "curtido: ");
                    holder.like.setImageResource(R.drawable.uc_coracao_cheio);
                    like[0] = true;

                }else {
                    Log.d("recuperar like", "ncurtido: ");
                    holder.like.setImageResource(R.drawable.ic_coracao_vazio);
                    like[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("recuperar like", "ncurtido: cancelled");
                holder.like.setImageResource(R.drawable.ic_coracao_vazio);
                like[0] = false;
            }
        });


        //curtir e descurtir
        scaleAnim = AnimationUtils.loadAnimation(item.getContext(), R.anim.anima_c);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", ""+like);
                if (like[0]) {
                    holder.like.setImageResource(R.drawable.ic_coracao_vazio);
                    like[0] = false;
                    int n = Integer.valueOf(holder.ncurtidas.getText().toString()) - 1;
                    if (n<0){
                        n=0;
                    }
                    holder.ncurtidas.setText(String.valueOf(n));
                    holder.ncurtidas.setTextColor(ContextCompat.getColor(v.getContext(), R.color.Cinza));
                    v.startAnimation(scaleAnim);
                    ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("curtidas").child(avaliacao.getIdAvaliacao()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("curtir", "descurtido: ");
                                //diminuir n curtidas
                                trasaction(-1,avaliacao);

                            } else task.getException().printStackTrace();

                        }
                    });

                } else if (!like[0]){
                    holder.like.setImageResource(R.drawable.uc_coracao_cheio);
                    like[0] = true;
                    int n = Integer.valueOf(holder.ncurtidas.getText().toString()) + 1;
                    holder.ncurtidas.setText(String.valueOf(n));
                    holder.ncurtidas.setTextColor(ContextCompat.getColor(v.getContext(), R.color.Vermelho));
                    v.startAnimation(scaleAnim);
                    ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("curtidas").child(avaliacao.getIdAvaliacao()).setValue(avaliacao.getIdAvaliacao()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("curtir", "curtido: ");
                                //aumentar numero de avaliacoes do local
                                trasaction(+1,avaliacao);

                            } else task.getException().printStackTrace();

                        }
                    });

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return avaliacaos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon, pin, like, report;
        private TextView nome, numero, nivel, data, texto, condicao, ncurtidas;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            pin = itemView.findViewById(R.id.pinA);
            like = itemView.findViewById(R.id.likA);
            nome = itemView.findViewById(R.id.nomeA);
            numero = itemView.findViewById(R.id.nA);
            nivel = itemView.findViewById(R.id.nivelA);
            data = itemView.findViewById(R.id.dataA);
            texto = itemView.findViewById(R.id.textView22);
            condicao = itemView.findViewById(R.id.condicaoA);
            recyclerView = itemView.findViewById(R.id.recA);
            ncurtidas = itemView.findViewById(R.id.ncurtidas);

        }
    }
    public void trasaction(int i,Avaliacao a){
        Avaliacao.REF_AVALIACAO.child(a.getIdAvaliacao()).child("curtidasAvaliacao").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer valorAtual = currentData.getValue(Integer.class);
                if (valorAtual == null) {
                    // Se o valor ainda não existe, não faz nada
                    return Transaction.success(currentData);
                }
                currentData.setValue(valorAtual+i);
                if (currentData.getValue(Integer.class)<0){
                    currentData.setValue(0);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e("curtir", "Erro ao atualizar avaliacao: " + error.getMessage());
                } else if (committed) {
                    Log.d("curtir", "avaliacao somada");
                }
            }
        });

    }

}
