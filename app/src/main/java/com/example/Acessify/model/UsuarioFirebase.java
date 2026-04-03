package com.example.Acessify.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {
    public static FirebaseUser getUsuarioAtual()
    {
        FirebaseAuth usuario = ConfiguraçaoFirebase.getAuth();
        return usuario.getCurrentUser();
    }
    public static String getIdUser(){
        return getUsuarioAtual().getUid();
    }
    public static Usuario getUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(firebaseUser.getUid());
        usuario.setEmailUsuario(firebaseUser.getEmail());
        usuario.setNomeUsuario(firebaseUser.getDisplayName());
        return usuario;
    }
    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();
            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
