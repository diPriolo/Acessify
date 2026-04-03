package com.example.Acessify.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.Acessify.Callback.LocaisCallback;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterLocaisSalvos;
import com.example.Acessify.adapter.AdapterMapaProximos;
import com.example.Acessify.bottomSheet.MyBottomSheet;
import com.example.Acessify.helper.RecyclerItemClickListener;
import com.example.Acessify.model.Local;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomerVermaisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomerVermaisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView vermenos;
    RecyclerView recyclerMais;
    private List<Local> locaisPopulares = new ArrayList<>();
    int quant = 15; // quantidade de locais buscados
    AdapterLocaisSalvos adapter;


    public HomerVermaisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomerVermaisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomerVermaisFragment newInstance(String param1, String param2) {
        HomerVermaisFragment fragment = new HomerVermaisFragment();
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
        return inflater.inflate(R.layout.fragment_homer_vermais, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setClickable(true);
        view.setFocusable(true);
        //criar recycler
        recyclerMais = view.findViewById(R.id.recyclerMais);
        recyclerMais.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerMais.setHasFixedSize(true);
        vermenos = view.findViewById(R.id.verMenos);
        vermenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        Local.resgatarPopularesMais(new LocaisCallback() {
            @Override
            public void CallbackLocais(List<Local> locais) {
                locaisPopulares.clear();
                locaisPopulares.addAll(locais);
                if (adapter == null){
                    adapter = new AdapterLocaisSalvos(locaisPopulares);
                    recyclerMais.setAdapter(adapter);
                }
            }
        },quant);
        recyclerClick();
        super.onViewCreated(view, savedInstanceState);
    }

    public void recyclerClick() {
        recyclerMais.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerMais, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Local local = locaisPopulares.get(position);
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