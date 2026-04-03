package com.example.Acessify.model;


import android.text.style.TtsSpan;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.Acessify.Callback.UsuarioCallback;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Usuario {
    private String datanascUsuario,emailUsuario,nomeUsuario,SobrenomeUsuario,senhaUsuario,idUsuario;







    public String getDatanascUsuario() {
        return datanascUsuario;
    }

    public void setDatanascUsuario(String datanascUsuario) {
        this.datanascUsuario = datanascUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSobrenomeUsuario() {
        return SobrenomeUsuario;
    }

    public void setSobrenomeUsuario(String sobrenomeUsuario) {
        SobrenomeUsuario = sobrenomeUsuario;
    }
    @Exclude
    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    //public static DatabaseReference LOCAL_SALVOS = ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("locaisSalvos");
   //public DatabaseReference avaliacoesRef = ConfiguraçaoFirebase.usuarioRef().child(idUsuario).child("avaliacoes");


    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void salvar(){
        DatabaseReference ref = ConfiguraçaoFirebase.getFirebase();
        ref.child("Usuarios").child(this.idUsuario).setValue(this);
        Log.i("User","Usuario salvo");
    }
    public static Usuario recuperarUsuario(String id, final UsuarioCallback usuarioCallback){
        ConfiguraçaoFirebase.usuarioRef().child(id).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario u = dataSnapshot.getValue(Usuario.class);
                    usuarioCallback.onRecuperado(u);
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " +e.getMessage());
            }
        });
        return null;
    }



}
