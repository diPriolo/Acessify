package com.example.Acessify.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.Acessify.Callback.AvaliacaoCallback;
import com.example.Acessify.Callback.ImagenCallback;
import com.example.Acessify.R;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Avaliacao implements Serializable {
    @Exclude
    public static  DatabaseReference REF_AVALIACAO = ConfiguraçaoFirebase.getFirebase().child("avaliacoes");

    public boolean isPendente() {
        return pendente;
    }

    public void setPendente(boolean pendente) {
        this.pendente = pendente;
    }

    public boolean pendente;


    private String idAvaliacao;
    private String idUsuario;
    private String idLocal;
    private String notaAvaliacao;
    @Exclude

    public static StorageReference baseRef = ConfiguraçaoFirebase.getStorage().child("locais");






    private String textoAvaliacao;
    private String dataAvaliacao;



    private Float numeroNota;
    @Exclude
    public Float getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Float numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(String idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(String notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }

    public String getTextoAvaliacao() {
        return textoAvaliacao;
    }

    public void setTextoAvaliacao(String textoAvaliacao) {
        this.textoAvaliacao = textoAvaliacao;
    }

    public String getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(String dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getCondicaoAvaliacao() {
        return condicaoAvaliacao;
    }

    public void setCondicaoAvaliacao(String condicaoAvaliacao) {
        this.condicaoAvaliacao = condicaoAvaliacao;
    }

    public int getCurtidasAvaliacao() {
        return curtidasAvaliacao;
    }

    public void setCurtidasAvaliacao(int curtidasAvaliacao) {
        this.curtidasAvaliacao = curtidasAvaliacao;
    }
@Exclude
    public List<Uri> getFotosAvaliacao() {
        return fotosAvaliacao;
    }

    public void setFotosAvaliacao(List<Uri> fotosAvaliacao) {
        this.fotosAvaliacao = fotosAvaliacao;
    }

    private String condicaoAvaliacao;
    private int curtidasAvaliacao;
    private List<Uri> fotosAvaliacao;

    public Avaliacao() {
    }


    public void salvarAvaliacao(Local local,AvaliacaoCallback avaliacaoCallback){
        //realtime
        REF_AVALIACAO.child(this.getIdAvaliacao()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            Boolean sts;
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    sts = true;
                }else
                {
                   sts = false;
                }
                avaliacaoCallback.onAvaliacaoFeita(sts);
            }
        });
        //local aumentar cont (:
        int quant;
        if (local.getQuantAvaliacoes() != null){

           //resgatar quant avaliacao e adicionar 1
            quant = Integer.valueOf(local.getQuantAvaliacoes());
            quant++;
            //media
            String m = mediacalc(quant,local);
            local.setMediaNota(m);

            Log.d("salvarAvaliacao","tem avaliacao");
            Log.d("salvarAvaliacao","media"+local.getMediaNota());


        }else{

            quant =1;
            local.setMediaNota(String.valueOf(numeroNota));

            Log.d("salvarAvaliacao","n tem avaliacao");
            //media
            local.setMediaNota(numeroNota.toString());
            Log.d("salvarAvaliacao","media"+local.getMediaNota());

        }
        //salvar fotos
        if (fotosAvaliacao != null){
            StorageReference imagensRef = baseRef.child(idLocal).child(idAvaliacao);
            new Thread(()->{ //converter imagen reduzida
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap image;
                    //salvar imagens
                    for (Uri img: fotosAvaliacao){
                        baos.reset();
                        image = Picasso.get().load(img).resize(800,800).onlyScaleDown().get();
                        image.compress(Bitmap.CompressFormat.JPEG,70,baos);
                        byte[] byteArray = baos.toByteArray();
                        String id = UUID.randomUUID().toString()+".jpg";
                        StorageReference ref = imagensRef.child(id);
                        baos.close();
                        image.recycle();
                        ref.putBytes(byteArray).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    Log.d("salvar", "onComplete: ");

                            }
                        });
                        ;
                    }

                }catch (IOException e)
                {
                    e.printStackTrace();
                }}).start();
        }
    }
    public String mediacalc(int n,Local local){
            double media = Double.parseDouble(local.getMediaNota());
            double novaMedia = (((n-1)*media)+numeroNota)/n;
             double mediaRedonda = Math.round(novaMedia * 10.0) / 10.0;
            return String.valueOf(mediaRedonda);
    }
    public void recuperarFotos(ImagenCallback imagenCallback){
        List<Uri> img = new ArrayList<>();
        List<Task<Uri>> tarefas = new ArrayList<>();
        StorageReference ref = baseRef.child(this.idLocal).child(this.idAvaliacao);
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item: listResult.getItems()){
                    tarefas.add(item.getDownloadUrl());
                }
                if (tarefas.isEmpty()){
                    return;
                }

                Tasks.whenAllSuccess(tarefas).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        for (Object obj: objects){
                            img.add((Uri) obj);
                        }
                        imagenCallback.onImagensCarregadas(img);
                    }
                });
            }
        });
        Log.d("recuperFotos: ",img.size()+"");
    }
    public void recupererCurtidas(){
        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("curtidas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot data: task.getResult().getChildren()){
                    Log.d("TAG", "onComplete: ");
                }
            }
        });
    }
    public int recuperarIcon(){
        switch (notaAvaliacao){
            case Local.NIVEL_ACESSIVEL:
                return R.drawable.pinverde;
            case Local.NIVEL_INACESSIVEL:
                return R.drawable.pinvermelho;
            default:
                return R.drawable.pinamarelo;
        }
    }
    public Pair<Integer, Integer> recuperarNivel(){
        switch (notaAvaliacao){
            case Local.NIVEL_ACESSIVEL:
                return  new Pair<>(R.drawable.pinverde,R.color.Verde);
            case Local.NIVEL_INACESSIVEL:
                return new Pair<>(R.drawable.pinvermelho,R.color.Vermelho);
            default:
                return new Pair<>(R.drawable.pinamarelo,R.color.Amarelo);
        }
    }
}
