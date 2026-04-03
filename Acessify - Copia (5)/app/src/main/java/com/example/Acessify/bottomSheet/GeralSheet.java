package com.example.Acessify.bottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterGeralSheet;
import com.example.Acessify.model.Local;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeralSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeralSheet extends Fragment {
    private Local local = new Local();
    private TextView sobreLocal;
    private RecyclerView recyclerView;
    private ImageView cachorro,rota;
    private GridLayoutManager layoutManager;
    AdapterGeralSheet adapterGeralSheet;
    

    public GeralSheet(Local lc) {
        this.local = lc;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeralSheet() {
        // Required empty public constructor
    }
    public static GeralSheet newInstance(Local local) {
        GeralSheet fragment = new GeralSheet();
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
     * @return A new instance of fragment GeralSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static GeralSheet newInstance(String param1, String param2) {
        GeralSheet fragment = new GeralSheet();
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
        View view = inflater.inflate(R.layout.fragment_geral_sheet, container, false);
        sobreLocal = view.findViewById(R.id.descGeral);

        sobreLocal.setText(local.getSobreLocal());

        cachorro = view.findViewById(R.id.cachorroGeral);
        cachorro.setVisibility(View.GONE);
        rota = view.findViewById(R.id.rota);
        rota.setOnClickListener(v -> abrirRota());
        setRecyclerView(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void abrirRota() {

        Uri mapAppUri = Uri.parse("geo:" + local.getLat() + ","
                + local.getLon() + "?q="
                + local.getLat() + ","
                + local.getLon()
                + "(" + Uri.encode(local.getNomeLocal()) + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, mapAppUri);
        startActivity(intent);
    }

    public void setRecyclerView(View view){
        recyclerView = view.findViewById(R.id.recyclerGeralSheet);


        recyclerView.setHasFixedSize(false);
        if (local.resgatarNivelPessoas().size()== 1)
        {

             layoutManager = new GridLayoutManager(view.getContext(),1);
        }else{
             layoutManager = new GridLayoutManager(view.getContext(),2);
        }
        recyclerView.setLayoutManager(layoutManager);
        if (local.resgatarNivelPessoas().size() == 0){
            recyclerView.setVisibility(View.GONE);
            cachorro.setVisibility(View.VISIBLE);
            TextView text = view.findViewById(R.id.textView6);
            text.setText("Esse local não é acessivel");



        }else {
            adapterGeralSheet = new AdapterGeralSheet(local.resgatarNivelPessoas());
            recyclerView.setAdapter(adapterGeralSheet);
        }

    }
}