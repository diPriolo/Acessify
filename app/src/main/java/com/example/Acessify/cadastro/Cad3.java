package com.example.Acessify.cadastro;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Acessify.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cad3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cad3 extends Fragment {
    private DatePickerDialog datePickerDialog;
    private EditText dateEdit;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Cad3() {}

    public static Cad3 newInstance(String param1, String param2) {
        Cad3 fragment = new Cad3();
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
        // Inflate o layout e guarde a view
        View view = inflater.inflate(R.layout.fragment_cad3, container, false);

        // Pegue o EditText depois da view estar pronta
        dateEdit = view.findViewById(R.id.editData);


        initDatePicker(); // Agora o initDatePicker pode usar o dateEdit sem erro


        // Ação ao clicar no campo para abrir o calendário
        dateEdit.setOnClickListener(v -> datePickerDialog.show());
        dateEdit.setInputType(InputType.TYPE_NULL); // impede teclado


        return view;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            int yearAtual = cal.get(Calendar.YEAR);
            if ((yearAtual -year)<5 || (yearAtual -year)>150){
                Toast.makeText(getContext(),"Idade Invalida", Toast.LENGTH_SHORT).show();
                Log.i("Idade",(yearAtual-year)+"");
            }else {
                String data = makeStringData(dayOfMonth, month, year);
                dateEdit.setText(data);
                Log.i("Idade",(yearAtual-year)+"");
            }
        };


        int year = 2000;
        int month = 0;
        int day = 1;


        datePickerDialog = new DatePickerDialog(requireContext(), R.style.MySpinnerDatePicker, dateSetListener, year, month, day);
    }

    private String DataAtual() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeStringData(day, month, year);
    }
    public String getNomeUsuario() {
        EditText nome = getView().findViewById(R.id.EditNome);
        return nome.getText().toString();
    }
    public String getSobrenomeUsuario(){
        EditText sobrenome = getView().findViewById(R.id.EditSobrenome);
        return sobrenome.getText().toString();
    }
    public String getdataUsuario(){
        EditText data = getView().findViewById(R.id.editData);
        return data.getText().toString();
    }
    private String makeStringData(int dayOfMonth, int month, int year) {
        // Adicionando "/" para formatar a data corretamente
        return String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
    }


}
