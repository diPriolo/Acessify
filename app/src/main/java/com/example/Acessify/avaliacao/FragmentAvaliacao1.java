package com.example.Acessify.avaliacao;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.Acessify.R;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAvaliacao1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAvaliacao1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnContinuar;

    private RadioGroup radioGroup;
    private Local local;
    private Avaliacao avaliacao = new Avaliacao();

    public FragmentAvaliacao1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAvaliacao1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAvaliacao1 newInstance(String param1, String param2) {
        FragmentAvaliacao1 fragment = new FragmentAvaliacao1();
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
        return inflater.inflate(R.layout.fragment_avaliacao1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup = view.findViewById(R.id.groupV1);
        btnContinuar = view.findViewById(R.id.buttonContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificar()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("avalicao",avaliacao); //passar avaliacao
                    bundle.putParcelable("local",local);
                    FragmentAvaliacao2 frag = new FragmentAvaliacao2();
                    frag.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.containerAvaliacao, frag).
                            addToBackStack(null).commit();


                }
            }

        });


    }
    public Boolean verificar(){
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getContext(),"Informe a acessibilidade",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            int i = radioGroup.getCheckedRadioButtonId();
           if (i == R.id.radioAcessivel)avaliacao.setNotaAvaliacao(Local.NIVEL_ACESSIVEL);
           if (i == R.id.radioMedio)avaliacao.setNotaAvaliacao(Local.NIVEL_MEDIO);
           if (i == R.id.radioBaixa)avaliacao.setNotaAvaliacao(Local.NIVEL_INACESSIVEL);


            return true;
        }


    }
}