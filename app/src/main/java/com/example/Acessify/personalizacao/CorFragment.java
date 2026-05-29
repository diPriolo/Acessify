package com.example.Acessify.personalizacao;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Acessify.R;
import com.example.Acessify.model.Personagem;
import com.skydoves.transformationlayout.TransformationLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView btnP,btnC,btnO,btnA,btnB;
    TransformationLayout trP,trC,trO,trA,trB;
    Boolean isTransformed = true; //para nao abrir mais de uma ao mesmo tempo

    public CorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CorFragment newInstance(String param1, String param2) {
        CorFragment fragment = new CorFragment();
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
        return inflater.inflate(R.layout.fragment_cor, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanciarBotoes(view);
        instanciarBtnPele(view);
        instanciarBtnAcessorio(view);
        instanciarBtnBarba(view);
        instanciarBtnOculos(view);
        instanciarBtnCabelo(view);
    }

    public void instanciarBotoes(View view){

        btnP = view.findViewById(R.id.btnP);
        btnC = view.findViewById(R.id.btnC);
        btnO = view.findViewById(R.id.btnO);
        btnA = view.findViewById(R.id.btnA);
        btnB = view.findViewById(R.id.btnB);

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trP = view.findViewById(R.id.transformatioP);
                if (!trP.isTransformed() && isTransformed){
                    trP.startTransform();
                    isTransformed = false;
                }
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 trC = view.findViewById(R.id.transformatioC);
                if (!trC.isTransformed() && isTransformed){
                    isTransformed = false;
                    trC.startTransform();
                }
            }
        });

        btnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 trO = view.findViewById(R.id.transformatioO);
                if (!trO.isTransformed() && isTransformed){
                    trO.startTransform();
                    isTransformed = false;
                }
            }
        });

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 trA = view.findViewById(R.id.transformatioA);
                if (!trA.isTransformed() && isTransformed){
                    trA.startTransform();
                    isTransformed = false;
                }
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 trB = view.findViewById(R.id.transformatioB);
                if (!trB.isTransformed() && isTransformed){
                    trB.startTransform();
                    isTransformed = false;
                }
            }
        });
    }
    public void instanciarBtnPele(View view){
        int qntBotoes = Personagem.corPeles.size(); //numero de opcoes
        for(int i =1;i<=qntBotoes;i++){
            final int index =i-1;
            String opc = "P"+i; //opcao escolhida
            int id= getResources().getIdentifier(opc,"id",requireContext().getPackageName()); //pegar o id do botao
            ImageView btn = view.findViewById(id);
            btn.setImageTintList(ColorStateList.valueOf(Personagem.corPeles.get(index).first)); //setar a cor do botao
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //depois tem que adicionar um callback
                    btnP.setImageTintList(ColorStateList.valueOf(Personagem.corPeles.get(index).first)); //trocar cor do botao
                    trP.finishTransform(); //fechar seletor
                    isTransformed =true; //liberar os outros
                }
            });
        }
    }
    public void instanciarBtnCabelo(View view){
        int qntBotoes = Personagem.corFios.length; //numero de opcoes
        for(int i =1;i<=qntBotoes;i++){
            final int index =i-1;
            String opc = "C"+i; //opcao escolhida
            int id= getResources().getIdentifier(opc,"id",requireContext().getPackageName()); //pegar o id do botao
            ImageView btn = view.findViewById(id);
            btn.setImageTintList(ColorStateList.valueOf(Personagem.corFios[index])); //setar a cor do botao
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //depois tem que adicionar um callback
                    btnC.setImageTintList(ColorStateList.valueOf(Personagem.corFios[index])); //trocar cor do botao
                    trC.finishTransform();
                    isTransformed =true;
                    Log.d("TAG", "onClick: ");
                }
            });
        }
    }
    public void instanciarBtnOculos(View view){
        int qntBotoes = Personagem.corOculos.length; //numero de opcoes
        for(int i =1;i<=qntBotoes;i++){
            final int index =i-1;
            String opc = "O"+i; //opcao escolhida
            int id= getResources().getIdentifier(opc,"id",requireContext().getPackageName()); //pegar o id do botao
            ImageView btn = view.findViewById(id);
            btn.setImageTintList(ColorStateList.valueOf(Personagem.corOculos[index])); //setar a cor do botao
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //depois tem que adicionar um callback
                    btnO.setImageTintList(ColorStateList.valueOf(Personagem.corOculos[index])); //trocar cor do botao
                    trO.finishTransform();
                    isTransformed =true;
                }
            });
        }
    }
    public void instanciarBtnAcessorio(View view){
        int qntBotoes = Personagem.corAcessorio.length; //numero de opcoes
        for(int i =1;i<=qntBotoes;i++){
            final int index =i-1;
            String opc = "A"+i; //opcao escolhida
            int id= getResources().getIdentifier(opc,"id",requireContext().getPackageName()); //pegar o id do botao
            ImageView btn = view.findViewById(id);
            btn.setImageTintList(ColorStateList.valueOf(Personagem.corAcessorio[index])); //setar a cor do botao
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //depois tem que adicionar um callback
                    btnA.setImageTintList(ColorStateList.valueOf(Personagem.corAcessorio[index])); //trocar cor do botao
                    trA.finishTransform();
                    isTransformed =true;
                }
            });
        }
    }
    public void instanciarBtnBarba(View view){
        int qntBotoes = Personagem.corFios.length; //numero de opcoes
        for(int i =1;i<=qntBotoes;i++){
            final int index =i-1;
            String opc = "B"+i; //opcao escolhida
            int id= getResources().getIdentifier(opc,"id",requireContext().getPackageName()); //pegar o id do botao
            ImageView btn = view.findViewById(id);
            btn.setImageTintList(ColorStateList.valueOf(Personagem.corFios[index])); //setar a cor do botao
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //depois tem que adicionar um callback
                    btnB.setImageTintList(ColorStateList.valueOf(Personagem.corFios[index])); //trocar cor do botao
                    trB.finishTransform();
                    isTransformed =true;
                }
            });
        }
    }

}