package com.example.Acessify;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.config.Permissoes;
import com.example.Acessify.frags.HomePage;
import com.example.Acessify.frags.MapaFragment;
import com.example.Acessify.frags.PerfilFragment;
import com.example.Acessify.frags.SalvosFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;
    private ImageView openDrawer;
    FirebaseAuth auth = ConfiguraçaoFirebase.getAuth();

    private String[] permissoes = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };
    private Fragment[] fragment = new Fragment[]{new HomePage(),new MapaFragment(),new SalvosFragment(),new PerfilFragment()};

    private FragmentContainer menuContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.roxo));
        }
        OfflineActivity.abrirOff(this,this);

        bottomNavigate(this);
        drawerLayout();
        Permissoes.validarPermissoes(permissoes, this, 1);
        containerFrag(0);


    }

        public void bottomNavigate(Activity activity) {
            //comeco bottom navigte
            bottomNavigationView = findViewById(R.id.bottomNavigation);
            //default fragmaente getSupportFragmentManager().beginTransaction().replace(R.id.container,cadFragment).commit();
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int i;
                   switch (menuItem.getTitle().toString()){
                       case "home":
                           OfflineActivity.abrirOff(getApplicationContext(),activity);
                           i =0;
                           break;
                       case "mapa":
                           i=1;
                           break;
                       case "salvos":
                           OfflineActivity.abrirOff(getApplicationContext(),activity);
                           i=2;
                           break;
                       case "conta":
                           OfflineActivity.abrirOff(getApplicationContext(),activity);
                           i=3;
                           break;
                       default:
                           OfflineActivity.abrirOff(getApplicationContext(),activity);
                           i=0;
                           break;


                   }
                    containerFrag(i);
                    return true;
                }
            });
            //fim bottom navigate
        }

    public void drawerLayout() {
        //drawer layout
        NavigationView navigationView = findViewById(R.id.nav_view);
        openDrawer = findViewById(R.id.btnNavigate);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.close();
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Log.d("NavigatesView", menuItem.getTitle().toString());

                if (menuItem.getItemId() == R.id.sairlateral) {
                    drawerLayout.close();
                }
                if (menuItem.getItemId() == R.id.duvidas){
                    duvidas();
                }
                if (menuItem.getItemId() == R.id.fale){
                    contato();
                }
                if (menuItem.getItemId() == R.id.config) {
                    config();
                }
                if (menuItem.getItemId() == R.id.sair) {
                    sair();
                }

                return false;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao();
            }
        }
    }

    public void alertaPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Permissoes Negadas").setMessage("é necessário autorizar as permissões para utilizar o app")
                .setCancelable(false).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void containerFrag(int i){
        getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer,fragment[i]).commit();
    }

    public void sair()
    {
        auth.signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
    public void duvidas(){
        startActivity(new Intent(this,DuvidaActivity.class));
    }
    public void config(){
        drawerLayout.close();

        startActivity(new Intent(this,ConfigActivity.class));
    }
    public void contato(){
        drawerLayout.close();

        startActivity(new Intent(this,ContatoActivity.class));
    }


}