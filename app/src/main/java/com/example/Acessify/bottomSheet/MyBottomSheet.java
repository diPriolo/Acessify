package com.example.Acessify.bottomSheet;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Acessify.Callback.CapaCallback;
import com.example.Acessify.config.ConfiguraçaoFirebase;
import com.example.Acessify.model.Usuario;
import com.example.Acessify.model.UsuarioFirebase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import androidx.viewpager2.widget.ViewPager2;

import com.example.Acessify.R;
import com.example.Acessify.model.Local;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.common.subtyping.qual.Bottom;

public class MyBottomSheet extends BottomSheetDialogFragment {
    private Local local;
    private TextView title,nota,avaliacoesn,subNome;

    private ImageView capa,icon,icSalvar;
    private boolean salvar;
    public MyBottomSheet() { }

    public static MyBottomSheet newInstance(Local local) {
        MyBottomSheet sheet = new MyBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable("local", local);
        sheet.setArguments(bundle);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);
        if (getArguments() != null) {
            local = getArguments().getParcelable("local");
        }
        title = view.findViewById(R.id.tituloSheet);
        subNome = view.findViewById(R.id.descSheet);
        subNome.setText(local.getSubNomeLocal());
        title.setText(local.getNomeLocal());
        capa = view.findViewById(R.id.imageViewCapaSheet);
        avaliacoesn = view.findViewById(R.id.avaliacoesNumero);
        nota = view.findViewById(R.id.notaSheet);
        icon = view.findViewById(R.id.nivel);
        icSalvar = view.findViewById(R.id.icSalvar);

        atualizarSalvo();
        if (local.getCapa() != null){
            Picasso.get().load(local.getCapa()).into(capa);
        }else {
            local.resgatarCapa(new CapaCallback() {
                @Override
                public void onCapaCarregada(Uri img) {
                    Picasso.get().load(img).into(capa);
                }
            });
        }
        icon.setImageResource(local.resgatarIcon());
        if (local.getMediaNota() == null){
            avaliacoesn.setText("");
            nota.setText("Nenhuma Avaliação");
        }else {
            avaliacoesn.setText("("+local.getQuantAvaliacoes()+")");
            nota.setText(local.getMediaNota());
        }

        return  view;
    }

    private void atualizarSalvo() {
        ConfiguraçaoFirebase.usuarioRef().child(UsuarioFirebase.getIdUser()).child("locaisSalvos").child(local.getIdLocal()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   Log.d("salva", "ta salvo ");
                   salvar = false;
                   icSalvar.setImageResource(R.drawable.book_mark_marcado);
               }else {
                   Log.d("salva", "n ta salvo ");
                   salvar = true;
                   icSalvar.setImageResource(R.drawable.book_mark);
               }

               icSalvar.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                      Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anima_c);

                       if (salvar){
                           local.salvarLocal();
                           salvar = false;
                           icSalvar.setImageResource(R.drawable.book_mark_marcado);

                       }else {
                           local.desalvarLocal();
                           salvar = true;
                           icSalvar.setImageResource(R.drawable.book_mark);
                       }
                       v.startAnimation(scaleAnim);




                   }
               });

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fechar ao clickar
        View bottomSheet = (View) view.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setHideable(true);


        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tab);
        //adapter
        ViewPageAdapter adapter = new ViewPageAdapter(this, local);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {



                switch (i){
                    case 0: tab.setText("Geral");
                    break;
                    case 1: tab.setText("Detalhes"); break;
                    case 2: tab.setText("Avaliações"); break;
                }
            }
        }).attach();
        view.findViewById(R.id.handle).setOnClickListener(v -> dismiss());
    }
    public void expandBottomSheet() {
        FrameLayout bottomSheet = getDialog().findViewById(
                com.google.android.material.R.id.design_bottom_sheet
        );
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null){
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            behavior.setSkipCollapsed(true);

            behavior.setFitToContents(true);
        }
    }
}
