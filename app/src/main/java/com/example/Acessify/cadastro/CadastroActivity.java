package com.example.Acessify.cadastro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.Acessify.MenuActivity;
import com.example.Acessify.R;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Usuario;
import com.example.Acessify.model.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private Button continuar;
    private ImageView wave, logo, borda;
    private TextView nome;


    private boolean anime = true;

    private Usuario usuario = new Usuario();


    private int frag = 0;
    private String datanascUsuario, emailUsuario, nomeUsuario, SobrenomeUsuario, senhaUsuario, idUsuario;

    private FirebaseAuth auth;
    private Cad1 cad1 = new Cad1();
    private Fragment[] logs = new Fragment[]{new Cad1(), new Cad2(), new Cad3(), new Cad4()
            //adicionar lista de fragments
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //adicionar cad 1
        wave = findViewById(R.id.wave);
        logo = findViewById(R.id.Logo);
        borda = findViewById(R.id.Borda);
        nome = findViewById(R.id.EditNome);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.CadContainer, cad1)
                .commit();
        continuar = findViewById(R.id.Entrar);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (anime) {
                    anima();  //para animao nao repetir.
                }

                atualizarFragment();
                Log.i("atual frag", "" + frag);
                //quando clickar no continuar
            }
        });
        //listener  para quando voltar na primeiro fragment
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.CadContainer);
            if (current instanceof Cad1 & !anime) {
                nome.clearAnimation();
                wave.clearAnimation();
                logo.setVisibility(View.VISIBLE);
                borda.setVisibility(View.VISIBLE);  //tira as animacoes ao chegar na inicial
                anime = true;

            }
            if (current instanceof Cad2) {
                continuar.setText("Vamos lá");
            } else {
                continuar.setText("Continuar");

            }
        });
        //salvar dados


    }


    public void atualizarFragment() {

        if (frag < logs.length - 1) {
            boolean pre = true; //libera passar de tela
            //verifica qual o frag e se foi prenchido.
            if (frag == 2) //pegar os dados quando estiver no frag 2
            {
                Cad3 frag = (Cad3) getSupportFragmentManager().findFragmentByTag("cad3"); //cad3
                if (frag != null && frag.isVisible()) {
                    nomeUsuario = frag.getNomeUsuario().trim(); //trim remove espaços no inicio e fim
                    SobrenomeUsuario = frag.getSobrenomeUsuario().trim();
                    datanascUsuario = frag.getdataUsuario();
                    Log.i("Infos", nomeUsuario + " " + SobrenomeUsuario + " " + datanascUsuario);
                }
                if (!preenchidoCad3(nomeUsuario, SobrenomeUsuario, datanascUsuario)) {
                    pre = false; //bloquea se as infos nao tiverem
                }

            }


            if (pre) {
                frag = frag + 1;

                Fragment fragment = logs[frag];
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (frag ==1)
                {
                    ImageView image = cad1.getView().findViewById(R.id.imageView);
                    //animcao cachorro
                    transaction.addSharedElement(image,"cachorro");

                }

                transaction.replace(R.id.CadContainer, fragment, "cad" + (frag + 1));
                transaction.addToBackStack(null);
                transaction.commit();
                Log.i("TAG", "" + frag + "]]" + logs.length);

            }

        } else {
            if (frag == 3) //pegar os dados quando estiver no frag 2
            {
                Cad4 frag = (Cad4) getSupportFragmentManager().findFragmentByTag("cad4"); //cad3
                if (frag != null && frag.isVisible()) {
                    emailUsuario = frag.getEmail().trim();
                    senhaUsuario = frag.getSenha().trim();

                    Log.i("Infos", emailUsuario + " " + senhaUsuario + " " + frag.getConfirmSenha());
                    if (preenchidoCad4(emailUsuario, senhaUsuario, frag.getConfirmSenha())) {
                        //salvar user

                        auth = ConfiguraçaoFirebase.getAuth();
                        auth.createUserWithEmailAndPassword(emailUsuario, senhaUsuario).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    usuario.setIdUsuario(idUsuario);
                                    usuario.setDatanascUsuario(datanascUsuario);
                                    usuario.setEmailUsuario(emailUsuario);
                                    usuario.setNomeUsuario(nomeUsuario);
                                    usuario.setSenhaUsuario(senhaUsuario);
                                    usuario.setSobrenomeUsuario(SobrenomeUsuario);
                                    usuario.setIdUsuario(task.getResult().getUser().getUid());
                                    UsuarioFirebase.atualizarNomeUsuario(nomeUsuario);

                                    usuario.salvar();
                                    Toast.makeText(getApplicationContext(), "Cadastro Realizado", Toast.LENGTH_LONG).show();
                                    //passar nome
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    intent.putExtra("nome",usuario.getNomeUsuario());
                                    startActivity(intent);

                                    finish();
                                } else {
                                    Log.i("InfosE", emailUsuario + " " + senhaUsuario + " " + frag.getConfirmSenha());
                                    String excecao = "";
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        excecao = "Digite uma senha mais forte";
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        excecao = "digite um email valido";
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        excecao = "Email já cadastrado";
                                    } catch (Exception e) {
                                        String msg = e.getMessage();

                                        if (msg != null && msg.contains("PASSWORD_DOES_NOT_MEET_REQUIREMENTS")) {
                                            if (msg.contains("upper case")) {
                                                excecao = "A senha deve conter letra maiúscula";
                                            } else if (msg.contains("numeric")) {
                                                excecao = "A senha deve conter número";
                                            } else if (msg.contains("at least 6 characters")) {
                                                excecao = "A senha deve ter pelo menos 6 caracteres";
                                            } else {
                                                excecao = "A senha não atende aos requisitos";
                                            }
                                        } else {
                                            excecao = "Erro ao cadastrar, tente novamente";
                                        }


                                    }
                                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }


            }

        }
    }

    public void anima() {
        nome.clearAnimation();
        wave.clearAnimation();
        int[] posicao = new int[2];
        nome.getLocationOnScreen(posicao);

        int deslocamento = 64 - posicao[1];

        logo.setVisibility(View.INVISIBLE);
        borda.setVisibility(View.INVISIBLE);


        TranslateAnimation animNome = new TranslateAnimation(0, 0, 0, deslocamento);
        TranslateAnimation animFundo = new TranslateAnimation(0, 0, 0, deslocamento - 20);
        animNome.setDuration(900);
        animNome.setFillAfter(true);
        animFundo.setDuration(1000);
        animFundo.setFillAfter(true);

        wave.startAnimation(animFundo);
        nome.startAnimation(animNome);
        anime = false;


    }

    @Override
    public void onBackPressed() {
        if (frag >= 0) {
            frag--;
            super.onBackPressed(); // volta para o fragment anterior
        } else {
            super.onBackPressed(); // sai da activity
        }
    }

    //verificaçoes
    public Boolean preenchidoCad3(String nome, String sobrenome, String data) {
        boolean pre;
        if (!nome.equals("") && possuiNumero(nome)) {
            if (!sobrenome.equals("") && possuiNumero(sobrenome)) {
                if (!data.equals("")) {
                    pre = true;
                } else {
                    Toast.makeText(this, "Informe a data", Toast.LENGTH_SHORT).show();
                    pre = false;
                }
            } else {
                Toast.makeText(this, "Sobrenome Invalido", Toast.LENGTH_SHORT).show();
                pre = false;
            }
        } else {
            Toast.makeText(this, "Nome Invalido", Toast.LENGTH_SHORT).show();
            pre = false;
        }
        return pre;
    }

    public Boolean preenchidoCad4(String email, String senha, String confirmSenha) {
        boolean pre;
        if (!email.equals("")) {
            if (!senha.equals("")) {
                if (!confirmSenha.equals("")) {
                    if (senha.equals(confirmSenha)) {
                        pre = true;
                    } else {
                        Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                        pre = false;
                    }
                } else {
                    Toast.makeText(this, "Confirme a senha", Toast.LENGTH_SHORT).show();
                    pre = false;
                }
            } else {
                Toast.makeText(this, "Informe a senha", Toast.LENGTH_SHORT).show();
                pre = false;
            }
        } else {
            Toast.makeText(this, "Informe o email", Toast.LENGTH_SHORT).show();
            pre = false;
        }
        return pre;
    }

    public boolean possuiNumero(String texto) {
        return texto.matches("[\\p{L} ]+") && texto.length() > 2;

    }
}