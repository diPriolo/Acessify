package com.example.Acessify.personalizacao;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Acessify.R;
import com.example.Acessify.model.Personagem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalizacaoActivity extends AppCompatActivity {
    Button btnCriar, btnContinuar;
    MotionLayout motionLayout;
    Personagem personagem;
    Personalizacao_1Fragment perso1_frag = new Personalizacao_1Fragment();
    Personalizacao_2fragment perso2_frag = new Personalizacao_2fragment();

    int sequencia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personalizacao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.motion_personalizacao), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        motionLayout = findViewById(R.id.motion_personalizacao);
        btnCriar = findViewById(R.id.Entrar);
        btnContinuar = findViewById(R.id.naoCriar);
        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequencia = 1;
                iniciarPersonalizacao();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container_personalizacao,perso1_frag).commit();
        //se sequncia igual a 0 btnContinuar fecha e termina o login se igual a 1 vira o de continuar
        btnContinuar.setOnClickListener(v-> continuarPersonalizacao());


    }

    private void continuarPersonalizacao() {
        if (sequencia == 0){
            finish();
        }else {


            if (sequencia==1){
                sequencia = 2;
               motionLayout.transitionToState(R.id.mid);
               salvarPersonalizacao1();


            }
            if (sequencia == 2){
               motionLayout.transitionToState(R.id.end);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_personalizacao,perso2_frag).commit();
                Log.d("ad", "continuarPersonalizacao: ");
                perso2_frag.resgatarPersonalizacao(personagem);



            }
        }
    }

    private void iniciarPersonalizacao() {
        motionLayout.transitionToEnd();
    }
    public void salvarPersonalizacao1(){
        personagem = new Personagem();
        personagem = perso1_frag.passarPersonagem();

    }


}
