package com.example.Acessify.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguraçaoFirebase {
private static FirebaseAuth autenticacao;
private static DatabaseReference database;
private static StorageReference  storage;

public static FirebaseAuth getAuth(){
    if (autenticacao == null){
        autenticacao = FirebaseAuth.getInstance();
     }
    return autenticacao;
    }
    public static DatabaseReference getFirebase(){
        if (database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
    public static DatabaseReference usuarioRef(){
     return getFirebase().child("Usuarios");
    }


    public static StorageReference getStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
