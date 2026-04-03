package com.example.Acessify.frags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.webauthn.Cbor;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.Acessify.Callback.LocaisCallback;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterLocaisSalvos;
import com.example.Acessify.bottomSheet.MyBottomSheet;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.helper.RecyclerItemClickListener;
import com.example.Acessify.model.Local;
import com.example.Acessify.model.Usuario;
import com.example.Acessify.model.UsuarioFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalvosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalvosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recSalvos;

    private List<Local> locaisSalvos = new ArrayList<>();
    private List<String> idsSalvos = new ArrayList<>();
    AdapterLocaisSalvos adapterLocaisSalvos;
    private ValueEventListener listener;
    private LinearLayout vazio;


    public SalvosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment salvos.
     */
    // TODO: Rename and change types and number of parameters
    public static SalvosFragment newInstance(String param1, String param2) {
        SalvosFragment fragment = new SalvosFragment();
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
        View v = inflater.inflate(R.layout.fragment_salvos, container, false);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    recSalvos.setVisibility(View.GONE);
                    vazio.setVisibility(View.VISIBLE);
                }
                locaisSalvos.clear();
                idsSalvos.clear();
                for(DataSnapshot s:snapshot.getChildren()){
                    String l = s.getValue(String.class);
                    idsSalvos.add(l);
                    if (idsSalvos.size() == snapshot.getChildrenCount()){
                        resgatarLocais();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        // Inflate the layout for this fragment
        vazio = view.findViewById(R.id.vazio);
        recSalvos = view.findViewById(R.id.recSalvos);
        recSalvos.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        recuperarSalvos();
        Log.d("sads", "onCreateView: ");
        recArrasta();
        recyclerClick();

    }

    public void recuperarSalvos(){

        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("locaisSalvos").addListenerForSingleValueEvent(listener);
    }
    public  void resgatarLocais() {

        int cont = idsSalvos.size();
        if (idsSalvos.isEmpty()){
            recSalvos.setVisibility(View.GONE);
            vazio.setVisibility(View.VISIBLE);
        }
        else
        {
            recSalvos.setVisibility(View.VISIBLE);
            vazio.setVisibility(View.GONE);

        }
        locaisSalvos.clear();
        for (String id: idsSalvos){
            Local.REF_LOCAL.child(id).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                  if (dataSnapshot.exists()){
                      Local l = dataSnapshot.getValue(Local.class);
                      locaisSalvos.add(l);

                          Log.d("TAG", "primiero");
                          adapterLocaisSalvos = new AdapterLocaisSalvos(locaisSalvos);
                          recSalvos.setAdapter(adapterLocaisSalvos);
                  }else {
                      recSalvos.setVisibility(View.GONE);
                      vazio.setVisibility(View.VISIBLE);
                  }

                }
            });

        }








    }
    public void recArrasta() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlag = ItemTouchHelper.START;
                return makeMovementFlags(dragFlags,swipeFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }


        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recSalvos);
    }
    public void excluirMovimentacao( RecyclerView.ViewHolder viewHolder){
        Local l;
        int position = viewHolder.getAdapterPosition();
        l = locaisSalvos.get(position);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remover Local dos Salvos?").setMessage("Certeza que deseja remover "+l.getNomeLocal()+" dos locais salvos?").setCancelable(false);
        alertDialog.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                l.desalvarLocal();
            }
        });
        alertDialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterLocaisSalvos.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    public void recyclerClick(){
        recSalvos.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recSalvos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                MyBottomSheet myBottomSheet = MyBottomSheet.newInstance(locaisSalvos.get(position));
                myBottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheet");

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (listener != null) {
            ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).removeEventListener(listener);
        }
    }
}