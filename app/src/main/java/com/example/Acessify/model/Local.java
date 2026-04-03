package com.example.Acessify.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.L;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.Acessify.Callback.CapaCallback;
import com.example.Acessify.Callback.ImagenCallback;
import com.example.Acessify.Callback.LocaisCallback;
import com.example.Acessify.Callback.LocalCallback;
import com.example.Acessify.R;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;


public class Local implements Parcelable {
    public Local() {
    }
    //======================================================
    //estaticos
    //======================================================
    public static DatabaseReference REF_LOCAL = ConfiguraçaoFirebase.getFirebase().child("locais");
    public static DatabaseReference REF_LOCALIZACAO = ConfiguraçaoFirebase.getFirebase().child("local_localizacao");

    public static final String NIVEL_INACESSIVEL = "inacessivel";

    public static final String NIVEL_ACESSIVEL = "acessivel";
    public static final String NIVEL_MEDIO = "medio";
    public static final String ZONA_SUL = "sul";
    public static final String ZONA_OESTE = "oeste";
    public static final String ZONA_LESTE = "leste";
    public static final String ZONA_NORTE = "norte";
    public static final String ZONA_CENTRO = "centro";
    //======================================================
    //constantes
    //======================================================
    private StorageReference imagensRef = ConfiguraçaoFirebase.getStorage().child("locais");

    private String nivelAcessibilidade;
    private String idLocal;
    private String nomeLocal;
    private String sobreLocal;
    private String nivelPessoa;
    private String nivelInfraestrura;
    private String mediaNota;
    private String zona;
    private String lat;
    private String lon;
    private String subNomeLocal;
    public Uri capa;
    //======================================================
    //gets e setters
    //======================================================

    public String getQuantAvaliacoes() {
        return quantAvaliacoes;
    }

    public void setQuantAvaliacoes(String quantAvaliacoes) {
        this.quantAvaliacoes = quantAvaliacoes;
    }

    private String quantAvaliacoes;

    public String getMediaNota() {
        return mediaNota;
    }

    public void setMediaNota(String mediaNota) {
        this.mediaNota = mediaNota;
    }



    public String getSubNomeLocal() {
        return subNomeLocal;
    }

    public void setSubNomeLocal(String subNomeLocal) {
        this.subNomeLocal = subNomeLocal;
    }
    public String getNivelAcessibilidade() {
        return nivelAcessibilidade;
    }

    public void setNivelAcessibilidade(String nivelAcessibilidade) {
        this.nivelAcessibilidade = nivelAcessibilidade;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getSobreLocal() {
        return sobreLocal;
    }

    public void setSobreLocal(String sobreLocal) {
        this.sobreLocal = sobreLocal;
    }

    public String getNivelPessoa() {
        return nivelPessoa;
    }

    public void setNivelPessoa(String nivelPessoa) {
        this.nivelPessoa = nivelPessoa;
    }

    public String getNivelInfraestrura() {
        return nivelInfraestrura;
    }

    public void setNivelInfraestrura(String nivelInfraestrura) {
        this.nivelInfraestrura = nivelInfraestrura;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Exclude
    public Uri getCapa() {
        return capa;
    }

    public void setCapa(Uri capa) {
        this.capa = capa;
    }
    //======================================================
    //parcelable
    //======================================================
    protected Local(Parcel in) {
        nivelAcessibilidade = in.readString();
        idLocal = in.readString();
        nomeLocal = in.readString();
        sobreLocal = in.readString();
        nivelPessoa = in.readString();
        nivelInfraestrura = in.readString();
        quantAvaliacoes = in.readString();
        mediaNota = in.readString();
        zona = in.readString();
        lat = in.readString();
        lon = in.readString();
        subNomeLocal = in.readString();
        capa = Uri.parse(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nivelAcessibilidade);
        dest.writeString(idLocal);
        dest.writeString(nomeLocal);
        dest.writeString(sobreLocal);
        dest.writeString(nivelPessoa);
        dest.writeString(nivelInfraestrura);
        dest.writeString(quantAvaliacoes);
        dest.writeString(mediaNota);
        dest.writeString(zona);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(subNomeLocal);
        if (capa != null){
            dest.writeString(capa.toString());
        }else capa = null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Local> CREATOR = new Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel in) {
            return new Local(in);
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };
    //======================================================
    //funcoes
    //======================================================
    public List<Local> resgatarPopulares(LocaisCallback callback) {

        List<Local> locais = new ArrayList<>();
        Query defaultQuery = REF_LOCAL.orderByChild("nomeLocal").limitToFirst(8); //mais para frente adicionar para ordenar por numero de avaliacoes
        defaultQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locais.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Local local = dados.getValue(Local.class);
                    locais.add(local);

                }
                callback.CallbackLocais(locais);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
    public static List<Local> resgatarPopularesMais(LocaisCallback callback,int total) {

        List<Local> locais = new ArrayList<>();
        Query defaultQuery = REF_LOCAL.orderByChild("nomeLocal").limitToFirst(total); //mais para frente adicionar para ordenar por numero de avaliacoes
        defaultQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locais.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Local local = dados.getValue(Local.class);
                    locais.add(local);

                }
                callback.CallbackLocais(locais);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }

    public List<Pair<String, Integer>> resgatarNivelPessoas() {
        List<Pair<String, Integer>> nivel = new ArrayList<>();
        String[] nivelPessoa = this.getNivelPessoa().split("");
        if (nivelPessoa[0].equals("1")) nivel.add(new Pair<>("Idoso", R.drawable.idoso_icon));
        if (nivelPessoa[1].equals("1")) nivel.add(new Pair<>("Gestante", R.drawable.gestante_icon));
        if (nivelPessoa[2].equals("1"))
            nivel.add(new Pair<>("Cadeirante", R.drawable.cadeirante_icon));
        if (nivelPessoa[3].equals("1")) nivel.add(new Pair<>("Cego", R.drawable.cego_icon));

        return nivel;
    }

    public List<String[]> resgatarAcesibilidae() {
        List<String[]> nivel = new ArrayList<>();
        String[] nivelInfra = this.getNivelInfraestrura().split(""); //1 cor 2nome 3 Texto 4 icon
        if (nivelInfra[0].equals("1")) { //elevador
            nivel.add(new String[]{"#4F7FE7", "Elevadores", "Os elevadores funcionam, recebem manutenção regularmente e são espaçosos!", String.valueOf(R.drawable.elevador_icon)});
        }
        if (nivelInfra[1].equals("1")) {//rampa
            nivel.add(new String[]{"#8753FF", "Rampas de acesso", "O local possui rampas de acesso que garantem a entrada e saída do local", String.valueOf(R.drawable.rampa_icon)});
        }
        if (nivelInfra[2].equals("1")) {//banheiro
            nivel.add(new String[]{"#F4901D", "Banheiro para PCDs", "Os banheiros para PCDs são limpos e grandes, equipados corretamente.", String.valueOf(R.drawable.banheiro_icon)});
        }
        if (nivelInfra[3].equals("1")) {//escadas
            nivel.add(new String[]{"#36B52F", "Escadas", "As escadas possuem corrimãos e fitas antiderrapantes em sua extensão.", String.valueOf(R.drawable.escada_icon)});
        }
        return nivel;
    }

    public void resgatarImagens(ImagenCallback imagenCallback) {
        List<Uri> img = new ArrayList<>();
        List<Task<Uri>> tarefas = new ArrayList<>();
        StorageReference ref = imagensRef.child(getIdLocal()).child("fotos_oficiais");

        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {


            @Override

            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    if (!item.getName().equals("capa.png")) {
                        tarefas.add(item.getDownloadUrl());
                    }

                }
                if (tarefas.isEmpty()) { //ve se a imagens
                    imagenCallback.onImagensCarregadas(new ArrayList<>());

                    return;
                }

                Tasks.whenAllSuccess(Tasks.whenAllSuccess(tarefas))
                        .addOnSuccessListener(results -> {
                            List<Uri> imagens = (List<Uri>) results.get(0);
                            imagenCallback.onImagensCarregadas(imagens);

                        });


            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("resgatarImagens", e.toString());
                imagenCallback.onImagensCarregadas(new ArrayList<>());

            }
        });

    }

    public static Local resgatarLocal(String idLocal, final LocalCallback callback) {
        REF_LOCAL.child(idLocal).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Local l = dataSnapshot.getValue(Local.class);
                callback.onCallback(l);
            }
        });
        return null;
    }

    public int resgatarIcon() {
        switch (this.nivelAcessibilidade) {
            case (Local.NIVEL_ACESSIVEL):
                return R.drawable.pinverde;
            case (Local.NIVEL_MEDIO):
                return R.drawable.pinamarelo;
            case (Local.NIVEL_INACESSIVEL):
                return R.drawable.pinvermelho;
        }
        return R.drawable.pinverde;
    }
    public void resgatarCapa(CapaCallback capaCallback) {
        StorageReference ref = imagensRef.child(getIdLocal()).child("fotos_oficiais");
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().equals("capa.png")) {
                       item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               capaCallback.onCapaCarregada(uri);
                           }
                       });
                    }

                }





            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("resgatarImagens", e.toString());
                capaCallback.onCapaCarregada(null);

            }
        });


    }
    public int resgatarCor(Context c) {
        switch (getNivelAcessibilidade()) {
            case (Local.NIVEL_ACESSIVEL):
                return ContextCompat.getColor(c, R.color.VerdeDark);
            case (Local.NIVEL_MEDIO):
                return ContextCompat.getColor(c, R.color.AmareloDark);
            case (Local.NIVEL_INACESSIVEL):
                return ContextCompat.getColor(c, R.color.VermelhoDark);
            default:
                return ContextCompat.getColor(c, R.color.Cinza);
        }
    }
    public  void salvarLocal(){
        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("locaisSalvos").child(this.idLocal).setValue(this.idLocal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("favoritar", "favoritado: ");
                }else {
                    Log.d("favoritar", task.getException().toString());

                }
            }
        });

    }
    public  void desalvarLocal(){
        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("locaisSalvos").child(this.idLocal).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("favoritar", "favoritado: ");
                }else {
                    Log.d("favoritar", task.getException().toString());

                }
            }
        });

    }
}

