package com.example.Acessify.bottomSheet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.Acessify.AvaliacoesActivity;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterAvaliacao;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvaliacaoSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvaliacaoSheet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Local local;
    private Button btnAdicionar;
    private RecyclerView rec;
    private View cachorro;

    private ArrayList<Avaliacao> avaliacaos= new ArrayList<Avaliacao>();
    private AdapterAvaliacao adapterAvaliacao;
    public AvaliacaoSheet(Local local) {
        this.local = local;
    }
    public AvaliacaoSheet() {

    }
    public static AvaliacaoSheet newInstance(Local local) {
    AvaliacaoSheet fragment = new AvaliacaoSheet();
    Bundle args = new Bundle();
    args.putParcelable("local", local);
    fragment.setArguments(args);
    return fragment;
}





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvaliacaoSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static AvaliacaoSheet newInstance(String param1, String param2) {
        AvaliacaoSheet fragment = new AvaliacaoSheet(new Local());
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
        if (getArguments() != null) {
            local = getArguments().getParcelable("local");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_avaliacao_sheet, container, false);
        inicializar(view);



        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAvaliacao();
            }
        });
        cachorro = view.findViewById(R.id.cachorrosheet);
        rec = view.findViewById(R.id.recAvaliacaoSheet);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandBottomSheet();
        resgatarAvaliacoes();
        Log.d("TAG", "onViewCreated: ");

    }

    public void inicializar(View view){
        btnAdicionar = view.findViewById(R.id.btnAdcionar);
    }
    public void abrirAvaliacao(){
        if (local != null){

            Intent intent = new Intent(getContext(),AvaliacoesActivity.class);
            intent.putExtra("idlocal",local.getIdLocal());


            startActivity(intent);

        }
    }
    public void inicializarRec(){
        adapterAvaliacao = new AdapterAvaliacao(avaliacaos);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rec.setAdapter(adapterAvaliacao);



    }
    public void resgatarAvaliacoes(){


        Avaliacao.REF_AVALIACAO.orderByChild("idLocal").equalTo(local.getIdLocal()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                   avaliacaoVazia();
                    return;
                }
                Log.d("recuperar avaliacoes",""+snapshot.getChildrenCount());
                if (snapshot.getChildrenCount()>0){
                    for (DataSnapshot dados: snapshot.getChildren()){
                        if (!dados.getValue(Avaliacao.class).pendente) {
                            avaliacaos.add(dados.getValue(Avaliacao.class));
                        }
                    }
                    inicializarRec();
                    if (avaliacaos.isEmpty()){
                        avaliacaoVazia();

                    }else {
                        cachorro.setVisibility(View.GONE);
                        rec.setVisibility(View.VISIBLE);


                    }

                }
                else {
                    //nao tem avaliacao]
                    Log.d("recuperar avaliacoes","nao tem");

                }


            }

            private void avaliacaoVazia() {

                cachorro.setVisibility(View.VISIBLE);
                rec.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("recuperar avaliacoes",""+error.getMessage());

            }
        });
    }

    //teste
    private void expandBottomSheet() {
        if (getParentFragment() instanceof MyBottomSheet) {
            ((MyBottomSheet) getParentFragment()).expandBottomSheet();
        }
    }
}