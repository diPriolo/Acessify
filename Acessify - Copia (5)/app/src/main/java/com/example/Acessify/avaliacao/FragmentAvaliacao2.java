package com.example.Acessify.avaliacao;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Acessify.Callback.AvaliacaoCallback;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterCreateAvaliacao;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;
import com.example.Acessify.model.UsuarioFirebase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAvaliacao2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAvaliacao2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, necessidade;
    private Avaliacao avaliacao;
    private TextView titulo,subTitulo,nomeUser, nivel;
    private EditText editAvaliacao;
    private Button btnFotos,btnPublicar;
    private Button nenhum,cadeira,muleta,idoso,gravida,cego;
    private ImageView iconNivel, iconUser;



    private Local local;
    private static final int REQUEST_CODE_READ_STORAGE = 100;
    Button[] opcoes;
    private boolean btnMarcado;
    //adicionar fotos
    private RecyclerView recyclerView;

    private List<Uri> fotos = new ArrayList<>();
    private List<Uri> fotosCache = new ArrayList<>();
    private AdapterCreateAvaliacao adapterAvaliacao = new AdapterCreateAvaliacao(fotos,this);
    private float numeroNota,mediaNota;

    ActivityResultLauncher<PickVisualMediaRequest> pickMultiple = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(6), uris ->{
        fotos = adapterAvaliacao.atualizarLista();
        Log.d("TAG", fotos.size()+"");
        if (uris != null){
            if ((fotos.size()+uris.size())<7){

                for (Uri uri : uris) {
                    Uri cacheUri = copiarParaCache(uri);
                    if (cacheUri != null) fotosCache.add(cacheUri);
                }
                btnFotos.setText("Adicione fotos do local("+(6-(fotos.size()+uris.size()))+")");
                fotos.addAll(uris);
                adapterAvaliacao.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }else {Toast.makeText(getContext(),"Máximo de 6 imagens",Toast.LENGTH_SHORT).show();}

        }else
        {
            Toast.makeText(getContext(),"Selecione uma mida",Toast.LENGTH_SHORT).show();
        }
    });

    public FragmentAvaliacao2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAvaliacao2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAvaliacao2 newInstance(String param1, String param2) {
        FragmentAvaliacao2 fragment = new FragmentAvaliacao2();
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
        local = getArguments().getParcelable("local");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        avaliacao =(Avaliacao) getArguments().getSerializable("avalicao");

        return inflater.inflate(R.layout.fragment_avaliacao2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializar(view);
        resgatarInfos();
        resgatarUser();
        botoesNecessidade();
        adicionarFotos();
        //salvar
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarInfos(view)){
                    salvarAvaliacao();
                }
            }
        });

    }
    public void inicializar(View view){
        // TextViews
        titulo = view.findViewById(R.id.textTitulo);
        subTitulo = view.findViewById(R.id.textSub);
        nomeUser = view.findViewById(R.id.nomeUser); //
        nivel = view.findViewById(R.id.txtnivel); //
        // EditText
        editAvaliacao = view.findViewById(R.id.editiAvaliacao);
        // Botões principais
        btnFotos = view.findViewById(R.id.btnAddFotos);
        btnPublicar = view.findViewById(R.id.buttonPublicar);
        // Botões de opções
        nenhum = view.findViewById(R.id.nenhuma);
        cadeira = view.findViewById(R.id.cadeira);
        muleta = view.findViewById(R.id.muleta);
        idoso = view.findViewById(R.id.idosa);
        gravida = view.findViewById(R.id.gravida);
        cego = view.findViewById(R.id.cego);

        iconNivel = view.findViewById(R.id.iconNivel);
        iconUser = view.findViewById(R.id.imgIcon);
        //array de botoes
        opcoes = new Button[]{nenhum,cadeira,muleta,idoso,gravida,cego};
        //adapter
        recyclerView = view.findViewById(R.id.adapaterCriar);
        //recyclerView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterAvaliacao);
        recyclerView.setVisibility(View.GONE);



    }
    public void resgatarUser(){
        nomeUser.setText(UsuarioFirebase.getUsuarioAtual().getDisplayName());
    }
    public void resgatarInfos(){
        nivel.setText(avaliacao.getNotaAvaliacao());
        switch (avaliacao.getNotaAvaliacao()){
            case(Local.NIVEL_ACESSIVEL):
                iconNivel.setImageResource(R.drawable.pinacessivel);
                nivel.setTextColor(ContextCompat.getColor(getContext(),R.color.Verde));
                numeroNota =5;
                break;
            case(Local.NIVEL_MEDIO):
                iconNivel.setImageResource(R.drawable.pinamarelo);
                nivel.setTextColor(ContextCompat.getColor(getContext(),R.color.Amarelo));
                numeroNota =4;

                break;
            case(Local.NIVEL_INACESSIVEL):
                iconNivel.setImageResource(R.drawable.pinvermelho);
                nivel.setTextColor(ContextCompat.getColor(getContext(),R.color.Vermelho));
                numeroNota =1;


                break;
        }
        //do local
        titulo.setText(local.getNomeLocal());
        subTitulo.setText(local.getSubNomeLocal());
    }
    public void botoesNecessidade(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == nenhum)marcarBotao(nenhum);
                if (v == cadeira)marcarBotao(cadeira);
                if (v ==  muleta )marcarBotao(muleta);
                if (v == idoso)marcarBotao(idoso);
                if (v == gravida)marcarBotao(gravida);
                if (v == cego)marcarBotao(cego);

            }
        };
        for (Button b:opcoes){
           b.setOnClickListener(listener);
        }
    }
    public void marcarBotao(Button button){
        for (Button b:opcoes){
            if (b!= button){
                b.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.Fundo)));
                b.setTextColor(ContextCompat.getColor(getContext(),R.color.TextoDarkAcent));
            }else {
                button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.TextoDarkAcent)));
                button.setTextColor(ContextCompat.getColor(getContext(),R.color.Fundo));
                necessidade = button.getText().toString();

            }

        }

    }

    public void adicionarFotos(){
        btnFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMultiple.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());

            }
        });
    }
    public void atualizaLista(List<Uri> l){
        fotos = l;
        btnFotos.setText("Adicione fotos do local("+(6-fotos.size())+")");
        Log.d("AG", "atualizaLista: "+fotos.size());
    }
    public Boolean verificarInfos(View view){
        Log.d("TAG", "verificarInfos: ");
        if (!necessidade.equals("")){
            if (!editAvaliacao.getText().toString().equals("")){

            }else {
                Toast.makeText(getContext(),"Escreva uma avaliação ",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(getContext(),"Informe sua necessidade",Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;
    }
    public void salvarAvaliacao(){
        avaliacao.setIdAvaliacao(Avaliacao.REF_AVALIACAO.push().getKey());
        avaliacao.setIdUsuario(UsuarioFirebase.getIdUser());

        avaliacao.setTextoAvaliacao(editAvaliacao.getText().toString());
        avaliacao.setCondicaoAvaliacao(necessidade);
        avaliacao.setCurtidasAvaliacao(0);
        avaliacao.setDataAvaliacao(recData());
        avaliacao.setPendente(true);
        avaliacao.setNumeroNota(numeroNota);
        if (!fotos.isEmpty()){
            avaliacao.setFotosAvaliacao(fotosCache);
        }
       avaliacao.setIdLocal(local.getIdLocal());
        avaliacao.salvarAvaliacao(local, new AvaliacaoCallback() {
            @Override
            public void onAvaliacaoFeita(Boolean estado) {
                if (estado){
                    Toast.makeText(getContext(),"Avalição enviada para análise",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else {
                    Toast.makeText(getContext(),"erro ao enviar avaliação, tente novamente",Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    public String recData(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formataData.format(date);

    }
    //arrumar picker
    private Uri copiarParaCache(Uri uri) {
        try {
            InputStream in = getContext().getContentResolver().openInputStream(uri);
            File arquivo = new File(getContext().getCacheDir(), "foto_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(arquivo);
            byte[] buffer = new byte[4096];
            int bytesLidos;
            while ((bytesLidos = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLidos);
            }
            in.close();
            out.close();
            return Uri.fromFile(arquivo);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}