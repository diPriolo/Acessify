package com.example.Acessify.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Acessify.Callback.LocaisCallback;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterHome;
import com.example.Acessify.bottomSheet.MyBottomSheet;
import com.example.Acessify.helper.RecyclerItemClickListener;
import com.example.Acessify.model.Local;
import com.example.Acessify.model.UsuarioFirebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Local local = new Local();
    private RecyclerView recyclerHome;
    private AdapterHome adapterHome;
    private TextView txtNome,vermais;
    private ImageView persona;
    private String nomeUser;
    private List<Local> locaisPopulares = new ArrayList();


    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        persona = view.findViewById(R.id.imgPerfil);

        //recycler
        recyclerHome = view.findViewById(R.id.recyclerHome);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerHome.setLayoutManager(manager);
        recyclerHome.setHasFixedSize(true);
        local.resgatarPopulares(new LocaisCallback() {
            @Override
            public void CallbackLocais(List<Local> locais) {
                Log.d("CallbackLocais","aberto");
                adapterHome = new AdapterHome(locais);
                recyclerHome.setAdapter(adapterHome);
                locaisPopulares = locais;


            }
        });
        /*car o nome
       Bundle bundle = requireActivity().getIntent().getExtras();
        if(bundle != null) {
            nomeUser = bundle.getString("nome");
        }else
        {
            nomeUser = UsuarioFirebase.getUsuarioAtual().getDisplayName();
        }
        txtNome = view.findViewById(R.id.textNome);
        txtNome.setText("Olá "+nomeUser+"!");

         */





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerClick();
        //ver mais
        maisver(view);


    }

    private void maisver(View view) {
        vermais = view.findViewById(R.id.verMenos);
        vermais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentTransaction f = getParentFragmentManager().beginTransaction();
               f.setCustomAnimations(R.anim.vermais_entrar,R.anim.vermais_sair,R.anim.vermais_entrar,R.anim.vermais_sair);
               f.add(R.id.menuContainer,new HomerVermaisFragment()).addToBackStack(null).commit();
            }
        });

    }

    public void recyclerClick() {
        recyclerHome.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerHome, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                local = locaisPopulares.get(position);
               MyBottomSheet myBottomSheet = MyBottomSheet.newInstance(local);
                myBottomSheet.show(getActivity().getSupportFragmentManager(),"BottomSheet" );

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }





}