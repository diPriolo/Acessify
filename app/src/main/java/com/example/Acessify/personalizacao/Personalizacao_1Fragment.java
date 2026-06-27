package com.example.Acessify.personalizacao;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.Acessify.R;
import com.example.Acessify.model.Personagem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personalizacao_1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personalizacao_1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ConstraintLayout constraintLayout;
    private ConstraintSet originalConstrain = new ConstraintSet();

    private String sexo,condicao,boca,nariz;
    private ImageView perso,olhoImg,bocaImg,narizImg;
    private MotionLayout frag1;


    public Personalizacao_1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personalizacao_1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Personalizacao_1Fragment newInstance(String param1, String param2) {
        Personalizacao_1Fragment fragment = new Personalizacao_1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personalizacao_1, container, false);
        perso = view.findViewById(R.id.perso);
        olhoImg = view.findViewById(R.id.olho_perso);
        bocaImg = view.findViewById(R.id.boca_perso);
        narizImg = view.findViewById(R.id.nariz_perso);
        sexo = "h";
        condicao = "n";
        nariz = "m";
        boca = "normal";
        frag1 = view.findViewById(R.id.frag_perso1);

        atualizarImagen();
        criarTab(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        constraintLayout = view.findViewById(R.id.perso1_redimen);
        ConstraintLayout pivotLayout = view.findViewById(R.id.perso1_constraint);
        pivotLayout.post(() -> { //arruma o pivot para redimensionar na hora de dar zoom
            pivotLayout.setPivotX(pivotLayout.getWidth() / 2f);
            pivotLayout.setPivotY(0f);
        });
        originalConstrain.clone(constraintLayout);
    }

    public void margemFeminina(View view){

        ConstraintSet constraintSet = new ConstraintSet();

        constraintSet.clone(constraintLayout);
        constraintSet.connect(
                R.id.rosto_1,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                R.id.rosto_1,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
        );
        int marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                44,
                getResources().getDisplayMetrics()
        );

        constraintSet.setMargin(R.id.rosto_1,ConstraintSet.TOP,marginTop);
        constraintSet.setMargin(R.id.rosto_1,ConstraintSet.START,1);
        constraintSet.applyTo(constraintLayout);
    }
    public void criarTab(View view){
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        // Criar as views
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View v1 = inflater.inflate(R.layout.perso_sexo, viewPager, false);
        View v2 = inflater.inflate(R.layout.perso_condicao, viewPager, false);
        View v3 = inflater.inflate(R.layout.perso_boca, viewPager, false);
        View v4 = inflater.inflate(R.layout.perso_nariz, viewPager, false);
        List<View> views = Arrays.asList(v1, v2, v3,v4);
        //adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        //Conectar TabLayout com ViewPager
        new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("genero");
                    break;

                case 1:
                    tab.setText("Condicão");
                    break;

                case 2:
                    tab.setText("boca");
                    break;
                case 3:
                    tab.setText("nariz");
                    break;
            }
        }).attach();

        escolha_sexo(v1); // estancia o v1
        escolha_condicao(v2);
        escolha_boca(v3);
        escolha_nariz(v4);
       viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);

               if (position ==2 || position ==3){
                   frag1.transitionToEnd();
               }
               else {
                   frag1.transitionToStart();
               }
           }
       });
        //ajustar tamanho do tabLayout
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenDpHeight = displayMetrics.heightPixels/displayMetrics.density;
        float tamanhoTAb = displayMetrics.density *(screenDpHeight - 555);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = (int) tamanhoTAb;

    }

    private void escolha_nariz(View v4) {
        v4.findViewById(R.id.nariz_p).setOnClickListener(v->{nariz ="p";atualizarImagen();});
        v4.findViewById(R.id.nariz_m).setOnClickListener(v->{nariz ="m";atualizarImagen();});
        v4.findViewById(R.id.nariz_g).setOnClickListener(v->{nariz ="g";atualizarImagen();});
        v4.findViewById(R.id.nariz_gg).setOnClickListener(v->{nariz ="gg";atualizarImagen();});
    }

    private void escolha_boca(View v3) {
        v3.findViewById(R.id.sorriso_fechado).setOnClickListener(v-> {boca = "fechada";atualizarImagen();});
        v3.findViewById(R.id.sorriso_grande).setOnClickListener(v-> {boca = "grande"; atualizarImagen();});
        v3.findViewById(R.id.sorriso_normal).setOnClickListener(v-> {boca = "normal"; atualizarImagen();});
    }

    private void escolha_condicao(View v2) {
        v2.findViewById(R.id.mulet).setOnClickListener(v -> {condicao = "mul"; atualizarImagen();mudarCorBotao( v2.findViewById(R.id.mulet));} );
        v2.findViewById(R.id.protesq).setOnClickListener(v ->{ condicao = "protesq"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.protesq));});
        v2.findViewById(R.id.protdir).setOnClickListener(v -> {condicao = "protdir"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.protdir));});
        v2.findViewById(R.id.portambas).setOnClickListener(v -> {condicao = "portambas"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.portambas));});
        v2.findViewById(R.id.cadeirante).setOnClickListener(v -> {condicao = "cadeirante"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.cadeirante));});
        v2.findViewById(R.id.gravi).setOnClickListener(v -> {condicao = "gravi"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.gravi));});
        v2.findViewById(R.id.button8).setOnClickListener(v -> {condicao = "n"; atualizarImagen();mudarCorBotao(v2.findViewById(R.id.button8));});

    }

    public void escolha_sexo(View view){
        ///pega a view e adicionas o onClick
        ImageButton masculino = view.findViewById(R.id.btnMasc);
        ImageButton feminino = view.findViewById(R.id.btnFem);
        masculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = "h";
                olhoImg.setImageResource(R.drawable.olho_masculino);
                masculino.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.TextoAcent)));
                feminino.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Cinza)));
                atualizarImagen();
                //voltar layout og
                originalConstrain.applyTo(constraintLayout);

            }
        });
        feminino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = "f";
                feminino.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.TextoAcent)));
                masculino.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Cinza)));
                margemFeminina(getView());
                olhoImg.setImageResource(R.drawable.olho_feminino);
                atualizarImagen();
            }
        });


    }

    public void atualizarImagen() {
        int img[] = new int[1];
        switch (sexo) {
            case "h":
                //condicao
                switch (condicao) {
                    case "mul":
                        img[0] = R.drawable.perso_masculino_muleta;
                        break;
                    case "protesq":
                        img[0] = R.drawable.perso_masculino_esquerda;
                        break;
                    case "protdir":
                        img[0] = R.drawable.perso_masculino_direita;
                        break;
                    case "portambas":
                        img[0] = R.drawable.perso_masculina_duas;
                        break;
                    case "cadeirante":
                        img[0] = R.drawable.perso_masculino_cadeira;
                        break;
                    default:
                        img[0] = R.drawable.perso_masculino_padrao;
                }
                break; //fim condicao
            //sexo
            case "f":
                switch (condicao) {
                    case "mul":
                        img[0] = R.drawable.perso_feminino_muleta;
                        break;
                    case "protesq":
                        img[0] = R.drawable.perso_feminino_esquerda;
                        break;
                    case "protdir":
                        img[0] = R.drawable.perso_feminino_direita;
                        break;
                    case "portambas":
                        img[0] = R.drawable.perso_feminino_duas;
                        break;
                    case "cadeirante":
                        img[0] = R.drawable.perso_feminino_cadeira;
                        break;
                    case "gravi":
                        img[0] = R.drawable.perso_feminino_gravida;
                        break;
                    default:
                        img[0] = R.drawable.perso_feminino_padrao;
                }
                break; //fim sexo
            default:
                img[0] = R.drawable.perso_masculino_padrao;
        }
        //boca
        switch (boca) {
            case "fechada":
                bocaImg.setImageResource(R.drawable.boca_serio);
                break;
            case "grande":
                bocaImg.setImageResource(R.drawable.boca_sorrindo);
                break;
            default:
                bocaImg.setImageResource(R.drawable.boca_normal);
        }
        //nariz
        switch (nariz){
            case "p":
                narizImg.setImageResource(R.drawable.nariz_p);
                break;
            case "g":
                narizImg.setImageResource(R.drawable.nariz_g);

                break;
            case "gg":
                narizImg.setImageResource(R.drawable.nariz_gg);

                break;
            default:
                narizImg.setImageResource(R.drawable.nariz_m);

        }
        perso.setImageResource(img[0]);

    }
    public void mudarCorBotao(Button button){
        View view = getView();
        List<Button> botoes = Arrays.asList(
                view.findViewById(R.id.mulet),
                view.findViewById(R.id.protesq),
                view.findViewById(R.id.protdir),
                view.findViewById(R.id.portambas),
                view.findViewById(R.id.cadeirante),
                view.findViewById(R.id.gravi),
                view.findViewById(R.id.button8)
        );
        for (Button btn: botoes){
            if (btn.equals(button)){
                //deixar colorido

                btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.TextoAcent)));

            }else {
                //volta pra cor original
                btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.Texto2)));

            }
        }

    }
    public Personagem passarPersonagem(){
        Personagem personagem = new Personagem();
        personagem.setBoca(boca);
        personagem.setNariz(nariz);
        personagem.setCondicao(condicao);
        personagem.setSexo(sexo);
        return personagem;
    }

}