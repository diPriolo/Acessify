package com.example.Acessify.cadastro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.Acessify.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cad4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cad4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText senha,senha1;

    private ImageView olhos,olhos1;

    public Cad4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cad4.
     */
    // TODO: Rename and change types and number of parameters
    public static Cad4 newInstance(String param1, String param2) {
        Cad4 fragment = new Cad4();
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
        View view = inflater.inflate(R.layout.fragment_cad4, container, false);
        senha1 = view.findViewById(R.id.editConfirmSenha);
        senha = view.findViewById(R.id.EditSenha);


        olhos = view.findViewById(R.id.olho2);
        olhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verSenha(senha,senha.getInputType());
            }
        });
        olhos1 = view.findViewById(R.id.olho);
        olhos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verSenha(senha1,senha1.getInputType());
            }
        });
        return view;


    }
    public String getEmail() {
        EditText email = getView().findViewById(R.id.EditEmail);
        return email.getText().toString();
    }
    public String getSenha(){
        EditText senha = getView().findViewById(R.id.EditSenha);
        return senha.getText().toString();
    }
    public String getConfirmSenha(){
        EditText confirmSenha = getView().findViewById(R.id.editConfirmSenha);
        return confirmSenha.getText().toString();
    }
    public void verSenha(EditText senha, int input){
        if (input == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
            Log.d("tag", "verSenha: visei");

            input = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }else
        {

            input = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }

        senha.setInputType(input);
        senha.setSelection(senha.getText().length());
        Log.d("Tag", "verSenha:"+input);
    }


}