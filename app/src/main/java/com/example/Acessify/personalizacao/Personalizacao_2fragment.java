package com.example.Acessify.personalizacao;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.tv.PesRequest;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.Acessify.Callback.CorCallback;
import com.example.Acessify.R;
import com.example.Acessify.model.Personagem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.skydoves.transformationlayout.TransformationLayout;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personalizacao_2fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personalizacao_2fragment extends Fragment implements CorCallback{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private boolean isZoom = true;

    private MotionLayout motionLayout;
    private ConstraintLayout constraintLayout;

    private int cabeloDrawble,acessorioDrawble,brincoDrawble,barbaDrawble;
    int corCabelo = 0,corOculos = 0,corPele = 0,corAcessorio = 0,corBarba =0;

    private ImageView perso,olhoImg,bocaImg,narizImg,cabeloImg,acessorioImg,brincoImg,barbaImg;
    private TransformationLayout transCor;
    private CardView cardCor;



    public Personalizacao_2fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personalizacao_2fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Personalizacao_2fragment newInstance(String param1, String param2) {
        Personalizacao_2fragment fragment = new Personalizacao_2fragment();
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
        View view = inflater.inflate(R.layout.fragment_personalizacao_2fragment, container, false);
        // Inflate the layout for this fragment
        constraintLayout = view.findViewById(R.id.perso1_redimen);
        perso = view.findViewById(R.id.perso);
        olhoImg = view.findViewById(R.id.olho_perso);
        bocaImg = view.findViewById(R.id.boca_perso);
        narizImg = view.findViewById(R.id.nariz_perso);
        cabeloImg = view.findViewById(R.id.cabelo_perso);
        acessorioImg = view.findViewById(R.id.acessorio_perso);
        brincoImg = view.findViewById(R.id.brinco_perso);
        barbaImg = view.findViewById(R.id.barba_perso);
        motionLayout = view.findViewById(R.id.frag_perso1);
        cabeloDrawble = R.drawable.cabelo_8;

        cardCor = view.findViewById(R.id.cardCor);
        transCor = view.findViewById(R.id.transCor); // esta dentro de um contraint para n colidir como motion layout


        view.findViewById(R.id.corBtn).setOnClickListener(new View.OnClickListener() { ///abrir car das cores
            @Override
            public void onClick(View v) {
                transCor.startTransform();
            }
        });
        //clickar e dar zoom
        perso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isZoom){
                    motionLayout.transitionToEnd();
                    isZoom = false;
                }else {
                    motionLayout.transitionToStart();
                    isZoom = true;
                }
            }
        });
        //alinhar para redimensionar
        ConstraintLayout pivotLayout = view.findViewById(R.id.perso1_constraint);
        pivotLayout.post(() -> { //arruma o pivot para redimensionar na hora de dar zoom
            pivotLayout.setPivotX(pivotLayout.getWidth() / 2f);
            pivotLayout.setPivotY(0f);
        });
      margemFeminina(view);
        criarTab(view);
        return view;
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
                5,
                getResources().getDisplayMetrics()
        );

        constraintSet.setMargin(R.id.rosto_1,ConstraintSet.TOP,marginTop);
        constraintSet.setMargin(R.id.rosto_1,ConstraintSet.START,0);
        constraintSet.applyTo(constraintLayout);
    }
    public void criarTab(View view) {
        tabLayout = view.findViewById(R.id.tabLayout2);
        viewPager = view.findViewById(R.id.viewPager2);
        // Criar as views
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View v1 = inflater.inflate(R.layout.perso_cabelo, viewPager, false);
        View v2 = inflater.inflate(R.layout.perso_oculos, viewPager, false);
        View v3 = inflater.inflate(R.layout.perso_brinco, viewPager, false);
        View v4 = inflater.inflate(R.layout.perso_barba, viewPager, false);
        List<View> views = Arrays.asList(v1, v2, v3, v4);
        //adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        //Conectar TabLayout com ViewPager
        new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("cabelo");
                    break;

                case 1:
                    tab.setText("acessorio");
                    break;

                case 2:
                    tab.setText("brinco");
                    break;
                case 3:
                    tab.setText("barba");
                    break;
            }
        }).attach();
        //zoom ao mexer
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                motionLayout.transitionToEnd();
                isZoom = true;
            }
        });
        //ajustar tamanho do tabLayout
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenDpHeight = displayMetrics.heightPixels/displayMetrics.density;
        float tamanhoTAb = displayMetrics.density *(screenDpHeight - 555);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = (int) tamanhoTAb;
        estanciarCabelo(v1);
        estanciarAcessorio(v2);
        estanciarBrinco(v3);
        estanciarBarba(v4);
    }
    //barba
    private void estanciarBarba(View view) {
        int[] ids = {
                R.id.ba1, R.id.ba2,R.id.ba3,R.id.ba4,R.id.ba5, R.id.ban
        };

        for (int id : ids) {
            view.findViewById(id).setOnClickListener(v -> atualizarBarba(id,ids,view));
        }
    }
    private void atualizarBarba(int idSelecionado, int[] ids, View view) {

        //mudar cor btn
        for (int i: ids){
            ImageView img = view.findViewById(i);
            //cor do botao
            if (i == idSelecionado){
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.TextoAcent)));
                if (i == R.id.ban){
                    barbaImg.setVisibility(View.GONE);
                }else {
                    recuperarIdBarba(view,idSelecionado);
                    barbaImg.setVisibility(View.VISIBLE);
                }
            }else {
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.Texto2)));

            }
        }

    }
    private void recuperarIdBarba(View view,int id){
        //recuperar id cabelo
        int numero = Integer.parseInt(
                view.getResources()
                        .getResourceEntryName(id)
                        .replace("ba", "")
        );
        //pegar id do drawble, pega o numero final do id ex 7 e adiciona o R.drawble.cabelo_

        barbaDrawble = view.getResources().getIdentifier(
                "barba_" + numero,
                "drawable",
                view.getContext().getPackageName()
        );
        barbaImg.setImageResource(barbaDrawble);
        atualizarCorBarba();
    }
    private void atualizarCorBarba() {

        //vector path cabelo
        if (barbaDrawble != 0) {
            VectorChildFinder vectorChildFinder = new VectorChildFinder(getContext(), barbaDrawble, barbaImg);
            VectorDrawableCompat.VFullPath path = vectorChildFinder.findPathByName("cor");
            path.setFillColor(Personagem.corFios[corBarba]);
            barbaImg.invalidate();
        }
    }

//brinco
    private void estanciarBrinco(View view) {
        int[] ids = {
                R.id.b1, R.id.b2, R.id.bn
        };

        for (int id : ids) {
            view.findViewById(id).setOnClickListener(v -> atualizarBrinco(id,ids,view));
        }
    }
    private void atualizarBrinco(int idSelecionado, int[] ids, View view) {

        //mudar cor btn
        for (int i: ids){
            ImageView img = view.findViewById(i);
            //cor do botao
            if (i == idSelecionado){
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.TextoAcent)));
                if (i == R.id.bn){
                    brincoImg.setVisibility(View.GONE);
                }else {
                    recuperarIdBrinco(view,idSelecionado);
                    brincoImg.setVisibility(View.VISIBLE);
                }
            }else {
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.Texto2)));

            }
        }

    }
    private void recuperarIdBrinco(View view,int id){
        //recuperar id cabelo
        int numero = Integer.parseInt(
                view.getResources()
                        .getResourceEntryName(id)
                        .replace("b", "")
        );
        //pegar id do drawble, pega o numero final do id ex 7 e adiciona o R.drawble.cabelo_

        brincoDrawble = view.getResources().getIdentifier(
                "brinco_" + numero,
                "drawable",
                view.getContext().getPackageName()
        );
        brincoImg.setImageResource(brincoDrawble);
        atualizarCorBrinco();
    }
    private void atualizarCorBrinco() {

        //vector path cabelo
        if (brincoDrawble != 0){

        VectorChildFinder vectorChildFinder = new VectorChildFinder(getContext(),brincoDrawble,brincoImg);
        VectorDrawableCompat.VFullPath path = vectorChildFinder.findPathByName("cor");
        path.setFillColor(Personagem.corAcessorio[corAcessorio]);
        brincoImg.invalidate();
    }
    }
//acessorios
    private void estanciarAcessorio(View view) {
        int[] ids = {
                R.id.a1, R.id.a2, R.id.a3, R.id.an
        };

        for (int id : ids) {
            view.findViewById(id).setOnClickListener(v -> atualizarAcessorio(id,ids,view));
        }
    }
    private void atualizarAcessorio(int idSelecionado, int[] ids, View view) {

        //mudar cor btn
        for (int i: ids){
            ImageView img = view.findViewById(i);
            //cor do botao
            if (i == idSelecionado){
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.TextoAcent)));
                if (i == R.id.an){
                    acessorioImg.setVisibility(View.GONE);
                }else {
                    recuperarIdAcessorio(view,idSelecionado);
                    acessorioImg.setVisibility(View.VISIBLE);
                }
            }else {
                img.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.Texto2)));

            }
        }

    }
    private void recuperarIdAcessorio(View view,int id){
        //recuperar id cabelo
        int numero = Integer.parseInt(
                view.getResources()
                        .getResourceEntryName(id)
                        .replace("a", "")
        );
        //pegar id do drawble, pega o numero final do id ex 7 e adiciona o R.drawble.cabelo_

        acessorioDrawble = view.getResources().getIdentifier(
                "acessorio_" + numero,
                "drawable",
                view.getContext().getPackageName()
        );
        acessorioImg.setImageResource(acessorioDrawble);
        atualizarCorCAcessorio();
    }
    private void atualizarCorCAcessorio() {

        //vector path cabelo
        if (acessorioDrawble != 0){
            VectorChildFinder vectorChildFinder = new VectorChildFinder(getContext(),acessorioDrawble,acessorioImg);
            VectorDrawableCompat.VFullPath path = vectorChildFinder.findPathByName("cor");
            path.setStrokeColor(Personagem.corOculos[corOculos]);
            acessorioImg.invalidate();
        }

    }


    //cabelo
       public void estanciarCabelo(View view){
           int[] ids = {
                   R.id.c1, R.id.c2, R.id.c3, R.id.c4,
                   R.id.c5, R.id.c6, R.id.c7, R.id.c8,
                   R.id.c9, R.id.c10, R.id.c11, R.id.c12,
                   R.id.c13, R.id.c14, R.id.c15, R.id.c16
           };

           for (int id : ids) {
               view.findViewById(id).setOnClickListener(v -> atualizarCabelo(id,ids,view));
           }

        }

        private void atualizarCabelo(int idSelecionado, int[] ids, View view) {

            //mudar cor btn
            for (int i: ids){
                ImageView img = view.findViewById(i);
                //cor do botao
                if (i == idSelecionado){
                    img.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.TextoAcent)));
                    recuperarIdCabelo(view,idSelecionado);
                }else {
                    img.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.Cinza)));

                }
            }

        }
        private void recuperarIdCabelo(View view,int id){
            //recuperar id cabelo
            int numero = Integer.parseInt(
                    view.getResources()
                            .getResourceEntryName(id)
                            .replace("c", "")
            );
            //pegar id do drawble, pega o numero final do id ex 7 e adiciona o R.drawble.cabelo_

            cabeloDrawble = view.getResources().getIdentifier(
                    "cabelo_" + numero,
                    "drawable",
                    view.getContext().getPackageName()
            );
            cabeloImg.setImageResource(cabeloDrawble);
            atualizarCorCabelo();
        }
        private void atualizarCorCabelo() {

            //vector path cabelo

                VectorChildFinder vectorChildFinder = new VectorChildFinder(getContext(),cabeloDrawble,cabeloImg);
                VectorDrawableCompat.VFullPath path = vectorChildFinder.findPathByName("cor");
                path.setFillColor(Personagem.corFios[corCabelo]);
                cabeloImg.invalidate();


        }
    private void atualizarCorPele(){

        VectorChildFinder vectorChildFinder = new VectorChildFinder(getContext(),R.drawable.perso_feminino_padrao,perso);
        VectorDrawableCompat.VFullPath path = vectorChildFinder.findPathByName("pele");
        path.setFillColor(Personagem.corPeles.get(corPele).first);
        perso.invalidate();
        narizImg.setImageTintList(ColorStateList.valueOf(Personagem.corPeles.get(corPele).second));
        bocaImg.setImageTintList(ColorStateList.valueOf(Personagem.corPeles.get(corPele).second));
        olhoImg.setImageTintList(ColorStateList.valueOf(Personagem.corPeles.get(corPele).second));
    }

    @Override
    public void onCorTrocada(int tipo, int cor) {
        Log.d("onCorTrocada","/"+tipo+"//"+cor);
        switch (tipo){
            case 1:
                Log.d("onCorTrocada","pele"+cor);
                corPele = cor;
                atualizarCorPele();
                break;
            case 2:
                Log.d("onCorTrocada","pele"+cor);
                corCabelo = cor;
                atualizarCorCabelo();
                break;
            case 3:
                Log.d("onCorTrocada","pele"+cor);
                corOculos = cor;
                atualizarCorCAcessorio();
                break;
            case 4:
                Log.d("onCorTrocada","pele"+cor);
                 corAcessorio= cor;
                atualizarCorBrinco();
                break;
            case 5:
                Log.d("onCorTrocada","pele"+cor);
                corBarba= cor;
                atualizarCorBarba();
                break;
            default:
                transCor.finishTransform();
                break;
        }
    }
    //acessorio
}