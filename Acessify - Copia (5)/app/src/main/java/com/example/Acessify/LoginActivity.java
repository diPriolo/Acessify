package com.example.Acessify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {
private TextInputEditText editEmail,editSenha;
private String email,senha;
private Usuario usuario = new Usuario();
private FirebaseAuth auth = ConfiguraçaoFirebase.getAuth();
private Button login;
private int inputtype;
private TextView esqueciTexto;

private ImageView olho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(600);
            changeBounds.setInterpolator(new AccelerateDecelerateInterpolator());
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);

            //animacao
        }
        OfflineActivity.abrirOff(this,this);

        inputtype = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        editEmail = findViewById(R.id.EditEmail);
        editSenha = findViewById(R.id.EditSenha);
        login = findViewById(R.id.login);
        olho = findViewById(R.id.olho);
        olho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verSenha();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (verificar()){
                   email = editEmail.getText().toString();
                   senha = editSenha.getText().toString();
                   Log.i("Infos",email+"///"+senha);
                   auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){

                               usuario.setEmailUsuario(email);
                               usuario.setSenhaUsuario(senha);
                               abrirTela();

                           }else
                           {

                               String excecao = "";
                               try {
                                   throw task.getException();
                               }catch (FirebaseAuthInvalidUserException e){
                                   excecao = "Email não cadastrado";
                               }
                               catch (FirebaseAuthInvalidCredentialsException e)
                               {
                                   excecao = "Email e senha não coincidem ";

                               }
                               catch (Exception e){
                                   excecao = "Erro ao verificar usuario";
                                   e.printStackTrace();
                               }
                               Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                           }
                       }
                   });

               }



            }
        });

        //esqueci a senha
        {
            esqueciTexto = findViewById(R.id.textEsqueci);
            esqueciTexto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String em = editEmail.getText().toString();
                    if (!em.equals("")&&em != null)
                    auth.sendPasswordResetEmail(em).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("TAG", "onComplete: "+task.isSuccessful()+"//"+task.getException());
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Link de redefinição enviado por email", Toast.LENGTH_LONG).show();

                            }else
                            {
                              Exception erro= task.getException();
                              String excecao;
                              if (erro instanceof FirebaseAuthInvalidCredentialsException)
                              {
                                  excecao = "email invalido";
                              }else {excecao = "erro ao enviar";}
                                Toast.makeText(LoginActivity.this,excecao, Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Informe o email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    public boolean verificar(){
        boolean pre;
        if (!editEmail.getText().toString().equals(""))
        {
            if (!editSenha.getText().toString().equals(""))
            {
                pre = true;
            }else {Toast.makeText(this,"Informe a senha",Toast.LENGTH_SHORT).show(); pre = false;}
        }else {Toast.makeText(this,"Informe o email",Toast.LENGTH_SHORT).show(); pre = false;}
        return pre;

    }
    public void abrirTela(){
        startActivity(new Intent(this,MenuActivity.class));
        finish();

    }
    public void verSenha(){
        if (inputtype == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
            inputtype = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }else
        {
            inputtype = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }

        editSenha.setInputType(inputtype);
        editSenha.setSelection(editSenha.getText().length());
    }
}