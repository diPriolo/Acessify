package com.example.Acessify;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class ContatoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contato);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.avlButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.roxo));
        }
        //instagram
        findViewById(R.id.insta).setOnClickListener(v -> abrirInsta());
        findViewById(R.id.youtube).setOnClickListener(v -> abrirYoutube());
        findViewById(R.id.email).setOnClickListener(v -> abrirEmail());
        OfflineActivity.abrirOff(this,this);

    }
    private void abrirYoutube() {
        String url = "https://www.youtube.com/channel/UCsIfxM6irSTCv1p9qLVuVDw"; // URL do canal

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.google.android.youtube");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Caso o app não esteja instalado, abre no navegador
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
    private void abrirInsta() {
        String url = "http://instagram.com/_u/acessify_";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Caso o app não esteja instalado, abre no navegador
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/acessify_")));
        }
    }
    private void abrirEmail() {
        String emailDestino = "acessifytcc@gmail.com";
        String assunto = "Contato pelo App";
        String texto = "Olá! Vim pelo aplicativo e gostaria de contatar a equipe do Acessify!";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailDestino));
        intent.putExtra(Intent.EXTRA_SUBJECT,assunto );
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Nenhum app de email encontrado", Toast.LENGTH_SHORT).show();
        }
    }

}