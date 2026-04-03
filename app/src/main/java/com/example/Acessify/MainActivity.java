package com.example.Acessify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import com.example.Acessify.cadastro.CadastroActivity;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

private Button login,cadastro;
private ImageView wave,logo,borda;
private TextView nome;
private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verificarLogin();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        OfflineActivity.abrirOff(this,this);



        login = findViewById(R.id.login);
        cadastro = findViewById(R.id.cadastro);
        wave = findViewById(R.id.wave);
        logo =  findViewById(R.id.Logo);
        borda =  findViewById(R.id.Borda);
        nome =  findViewById(R.id.EditNome);
        //jogar wave para cima





        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                Activity activity = (Activity) MainActivity.this;
                Pair[] pairs= new Pair[5];
                pairs[0] = new Pair<>(wave, ViewCompat.getTransitionName(wave));
                pairs[1] = new Pair<>(cadastro, ViewCompat.getTransitionName(cadastro));
                pairs[4] = new Pair<>(logo, ViewCompat.getTransitionName(logo));
                pairs[2] = new Pair<>(borda, ViewCompat.getTransitionName(borda));
                pairs[3] = new Pair<>(nome, ViewCompat.getTransitionName(nome));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pairs);
                activity.startActivity(intent,optionsCompat.toBundle());
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Activity activity = (Activity) MainActivity.this;
                Pair[] pairs= new Pair[5];
                pairs[0] = new Pair<>(wave, ViewCompat.getTransitionName(wave));
                pairs[1] = new Pair<>(cadastro, ViewCompat.getTransitionName(cadastro));
                pairs[4] = new Pair<>(logo, ViewCompat.getTransitionName(logo));
                pairs[2] = new Pair<>(borda, ViewCompat.getTransitionName(borda));
                pairs[3] = new Pair<>(nome, ViewCompat.getTransitionName(nome));

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pairs);
                activity.startActivity(intent,optionsCompat.toBundle());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarLogin();
    }

    public void  verificarLogin(){
        auth = ConfiguraçaoFirebase.getAuth();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            finish();
        }
    }

}