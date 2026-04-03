package com.example.Acessify.frags;

import static com.example.Acessify.model.Avaliacao.REF_AVALIACAO;
import static com.example.Acessify.model.Avaliacao.baseRef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Acessify.Callback.ImagenCallback;
import com.example.Acessify.Callback.LocalCallback;
import com.example.Acessify.Callback.UsuarioCallback;
import com.example.Acessify.ConfigActivity;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterImgAvaliacao;
import com.example.Acessify.adapter.AdapterPerfilAval;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.helper.RecyclerItemClickListener;
import com.example.Acessify.model.Avaliacao;
import com.example.Acessify.model.Local;
import com.example.Acessify.model.Usuario;
import com.example.Acessify.model.UsuarioFirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario usuario;
    private ArrayList<Pair<Avaliacao, Local>> avaliacaos = new ArrayList<>();
    private AdapterPerfilAval adapterPerfilAval;
    private RecyclerView recAval;
    private TextView vermaisAval,txtNome,textIdade,textAval,textSemAvaliacao;
    private LinearLayoutManager layoutManager;
    private ImageView imgconfig;


    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imgconfig = view.findViewById(R.id.imgConfig);
        recAval = view.findViewById(R.id.recAval);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recAval.setLayoutManager(layoutManager);
        recAval.setHasFixedSize(true);
        recAvalClick();
        vermaisAval = view.findViewById(R.id.vermaisAval);
        vermaisAval.setOnClickListener(v -> maisAvaliacoes());
        textSemAvaliacao = view.findViewById(R.id.textSAvalia);
        imgconfig.setOnClickListener(v -> config());
        if (usuario ==null){
            Usuario.recuperarUsuario(UsuarioFirebase.getIdUser(), new UsuarioCallback() {
                @Override
                public void onRecuperado(Usuario user) {
                    //perfil resgatado
                    Log.d("TAG", "onRecuperado: ");
                    usuario = user;
                    recueparAvaliacoes();
                    infoUser(view);

                }
            });
        }else {
            infoUser(view);
            recueparAvaliacoes();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void config() {
        startActivity(new Intent(getContext(), ConfigActivity.class));
    }

    public void infoUser(View view){
        txtNome = view.findViewById(R.id.txtNome);
        textIdade = view.findViewById(R.id.txtIdade);
        textAval = view.findViewById(R.id.txtAval);
        txtNome.setText(usuario.getNomeUsuario());
        textIdade.setText("-Idade: "+recuperarIdade()+" anos");

        ConfiguraçaoFirebase.usuarioRef().child(usuario.getIdUsuario()).child("avaliacoes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   textAval.setText("Avaliações feitas: "+dataSnapshot.getChildrenCount());
                }else {
                    textAval.setText("Nenhuma Avaliação Feita");
                }
            }
        });
    }
    private String recuperarIdade() {
        String data[] = usuario.getDatanascUsuario().split("/");
        LocalDate ano = LocalDate.of(Integer.valueOf(data[2]),Integer.valueOf(data[1]),Integer.valueOf(data[0]));
        Period p = Period.between(ano,LocalDate.now());
        return String.valueOf(p.getYears());
    }

    private void maisAvaliacoes() {
        if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL){
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            vermaisAval.setText(R.string.ver_menos);

        }else {
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            vermaisAval.setText(R.string.ver_mais);

        }



    }

    public  void recueparAvaliacoes() {
        avaliacaos.clear();
        REF_AVALIACAO.orderByChild("idUsuario").equalTo(usuario.getIdUsuario()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    //nenhuma avaliacao feita
                    recAval.setVisibility(View.GONE);
                    textSemAvaliacao.setVisibility(View.VISIBLE);
                    return;
                }
                textSemAvaliacao.setVisibility(View.GONE);

                for (DataSnapshot s : snapshot.getChildren()) {
                    Avaliacao av = s.getValue(Avaliacao.class); //resgata a avaliacao
                    if (!av.pendente){

                    Local.resgatarLocal(av.getIdLocal(), new LocalCallback() {
                        //resgata local
                        @Override
                        public void onCallback(Local local) {
                            Log.d("TAG", local.getNomeLocal());

                            avaliacaos.add(new Pair<>(av, local));
                            if (avaliacaos.size() == snapshot.getChildrenCount()) {
                                Log.d("TAG", "cahma" + avaliacaos.size());
                                //arrumar recycler
                                recAval.setVisibility(View.VISIBLE);
                                adapterPerfilAval = new AdapterPerfilAval(avaliacaos);
                                recAval.setAdapter(adapterPerfilAval);
                                textSemAvaliacao.setVisibility(View.GONE);
                                textSemAvaliacao.setVisibility(View.GONE);


                                Log.d("TAG", avaliacaos.size() + "]]" + snapshot.getChildrenCount());


                            }
                        }
                    });

                    }
                }
                if (avaliacaos.isEmpty()){
                    textSemAvaliacao.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void recAvalClick(){
        recAval.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recAval, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (avaliacaos.isEmpty()){
                    return;
                }
                Avaliacao av = avaliacaos.get(position).first;
                Local l = avaliacaos.get(position).second;
                //abrir bottom sheet
                BottomSheetDialog bottomSheetDialo = new BottomSheetDialog(getContext());
                View v = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_avaliacoes,null);
                //estanciar views
                ImageView b = v.findViewById(R.id.drag);

                Button btndelete = v.findViewById(R.id.btnDelete);
                ImageView btnVoltar = v.findViewById(R.id.btnVolta);
                ImageView icon = v.findViewById(R.id.imageView22);
                RecyclerView recImage = v.findViewById(R.id.recImg);
                TextView nomeLocal = v.findViewById(R.id.txtNomel);
                TextView subnome = v.findViewById(R.id.suvnomel5);
                TextView nomeuser = v.findViewById(R.id.nomeu);
                TextView nota = v.findViewById(R.id.notaavl);
                TextView data = v.findViewById(R.id.textData);
                TextView texto = v.findViewById(R.id.txttexto);
                TextView nCurtidas = v.findViewById(R.id.curtidas);
                TextView condicao = v.findViewById(R.id.condicao2);
                //por infos
                nomeLocal.setText(l.getNomeLocal());
                subnome.setText(l.getSubNomeLocal());
                nomeuser.setText(usuario.getNomeUsuario());
                nota.setText(av.getNotaAvaliacao());
                data.setText(av.getDataAvaliacao());
                texto.setText(av.getTextoAvaliacao());
                nCurtidas.setText(String.valueOf(av.getCurtidasAvaliacao()));
                condicao.setText(av.getCondicaoAvaliacao());
                nota.setTextColor(ContextCompat.getColor(v.getContext(), av.recuperarNivel().second));
                icon.setImageResource(av.recuperarIcon());
                //botoes
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Certeza que deseja excluir a avaliacao?").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletarAvaliacao(av,l,bottomSheetDialo);
                            }}).create().show();

                    }
                });
                btnVoltar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialo.dismiss();
                    }
                });
                //recycler imagens usar o mesmo da avaliacao
                av.recuperarFotos(new ImagenCallback() {
                    @Override
                    public void onImagensCarregadas(List<Uri> imgs) {
                        AdapterImgAvaliacao imgAvaliacao = new AdapterImgAvaliacao(imgs);
                        recImage.setAdapter(imgAvaliacao);
                        recImage.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    }
                });
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialo.dismiss();
                    }
                });
                //criar
                bottomSheetDialo.setContentView(v);
                bottomSheetDialo.show();



            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }
    public void deletarAvaliacao(Avaliacao avaliacao,Local local,BottomSheetDialog b){

        REF_AVALIACAO.child(avaliacao.getIdAvaliacao()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("rejeitar", "realtimeApagado ");
                recueparAvaliacoes();

            }

        });

        baseRef.child(local.getIdLocal()).child(avaliacao.getIdAvaliacao()).listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                for (StorageReference ref:task.getResult().getItems()){
                    ref.delete().addOnSuccessListener(aVoid ->
                    {
                        Log.d("deletar imagen", "onComplete: ");
                    });
                }
            }
        });


        //apagar os usuarios que curtiram
        ConfiguraçaoFirebase.usuarioRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot users: snapshot.getChildren()){
                   if (users.child("curtidas").child(avaliacao.getIdAvaliacao()).exists()){
                       users.child("curtidas").child(avaliacao.getIdAvaliacao()).getRef().removeValue();
                   }
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        b.dismiss();
    }

}

