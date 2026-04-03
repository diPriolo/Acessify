package com.example.Acessify;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.adapter.AdapterDuvidas;



public class DuvidaActivity extends AppCompatActivity {
    private RecyclerView recDuvidas;
    private AdapterDuvidas adapterDuvidas = new AdapterDuvidas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.roxo));
        }
        setContentView(R.layout.activity_duvida);
        recDuvidas = findViewById(R.id.recDuvidas);
        recDuvidas.setLayoutManager(new LinearLayoutManager(getApplication(),LinearLayoutManager.VERTICAL,false));
        recDuvidas.setHasFixedSize(true);
        recDuvidas.setAdapter(adapterDuvidas);
        findViewById(R.id.avlButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        OfflineActivity.abrirOff(this,this);




    }


}