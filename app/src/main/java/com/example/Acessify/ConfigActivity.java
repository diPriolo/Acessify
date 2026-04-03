package com.example.Acessify;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;
import com.example.Acessify.model.Usuario;
import com.example.Acessify.model.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class ConfigActivity extends AppCompatActivity {
    private ImageView fechar;
    private View contato,apagar,fundo;
    private int totalTarefas = 0;
    private int tarefasConcluidas = 0;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        OfflineActivity.abrirOff(this,this);

        fundo = findViewById(R.id.fundo);
        contato = findViewById(R.id.contato);
        apagar = findViewById(R.id.apagar);
        fechar = findViewById(R.id.fechar);
        dialog = new Dialog(this);
        //onclick
        apagar.setOnClickListener(v -> abrirApagarDialog());
        contato.setOnClickListener(v -> abrirContato());
        fundo.setOnClickListener(v-> encerrar());
        fechar.setOnClickListener(v->encerrar());
    }

    private void abrirApagarDialog() {
        dialog.setContentView(R.layout.apagar_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btncancelar = dialog.findViewById(R.id.btnDialogCancelar);
        Button btnApagar = dialog.findViewById(R.id.btnDialogApagar);
        btncancelar.setOnClickListener(v -> dialog.dismiss() );
        btnApagar.setOnClickListener(v -> apagarConta());
        dialog.show();

    }


    private void incrementar() {
        totalTarefas++;
    }

    private void finalizarTarefa(String id) {
        tarefasConcluidas++;
        if (tarefasConcluidas == totalTarefas) {
            Log.d("apagarConta", "Todas tarefas concluídas!");

           //apaga o auth
            UsuarioFirebase.getUsuarioAtual().delete().addOnCompleteListener(task -> {

                //apaga db
                ConfiguraçaoFirebase.usuarioRef()
                        .child(id)
                        .removeValue();

                //fecha
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            });
        }
    }

    private void apagarConta() {
        Usuario user = UsuarioFirebase.getUsuarioLogado();
        Log.d("apagarConta", "ai");


        //pegas as avaliacoes
        Avaliacao.REF_AVALIACAO
                .orderByChild("idUsuario")
                .equalTo(user.getIdUsuario())
                .get()
                .addOnSuccessListener(dataSnapshot -> {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        Avaliacao avaliacao = s.getValue(Avaliacao.class);

                        //apagar as imgs
                        incrementar();
                        Avaliacao.baseRef
                                .child(avaliacao.getIdLocal())
                                .child(avaliacao.getIdAvaliacao())
                                .listAll()
                                .addOnSuccessListener(listResult -> {
                                    for (StorageReference result : listResult.getItems()) {
                                        incrementar();
                                        result.delete()
                                                .addOnSuccessListener(aVoid -> finalizarTarefa(user.getIdUsuario()))
                                                .addOnFailureListener(e -> finalizarTarefa(user.getIdUsuario()));
                                    }
                                    finalizarTarefa(user.getIdUsuario()); // terminou o listAll()
                                });

                        //apagar as avaliacoes
                        incrementar();
                        s.getRef()
                                .removeValue()
                                .addOnSuccessListener(aVoid -> finalizarTarefa(user.getIdUsuario()));
                    }

                    // caso o usuário não tenha avaliações
                    if (!dataSnapshot.hasChildren()) {
                        // nenhuma tarefa = faz tudo direto
                        UsuarioFirebase.getUsuarioAtual().delete().addOnCompleteListener(task -> {
                            ConfiguraçaoFirebase.usuarioRef().child(user.getIdUsuario()).removeValue();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        });
                    }
                });
    }


    private void abrirContato() {
        finish();
        startActivity(new Intent(this,ContatoActivity.class));


    }

    private void encerrar() {
        finish();
    }

}