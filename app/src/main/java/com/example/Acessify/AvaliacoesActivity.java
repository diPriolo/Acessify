package com.example.Acessify;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.Acessify.Callback.LocalCallback;
import com.example.Acessify.avaliacao.FragmentAvaliacao1;
import com.example.Acessify.model.Local;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class AvaliacoesActivity extends AppCompatActivity {
    private FragmentContainer fragAvaliacao;
    private FragmentAvaliacao1 frag1;
    private ImageButton avlVoltar;
    private Local local = new Local();



    //adicionar foto


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avaliacoes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.roxo));
        }
        OfflineActivity.abrirOff(this,this);

       instanciarElementos();

        avlVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


    }
    public void instanciarElementos(){
        String id = getIntent().getStringExtra("idlocal");
        local.resgatarLocal(id, new LocalCallback() {
            @Override
            public void onCallback(Local l) {
                local = l;
                frag1 = new FragmentAvaliacao1(); //adicionar bunldle
                Bundle bundle = new Bundle();
                bundle.putParcelable("local",local);
                frag1.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.containerAvaliacao,frag1,"tela1" ).commit();

            }
        });

        avlVoltar = findViewById(R.id.avlButton);


    }


}