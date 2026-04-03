package com.example.Acessify.bottomSheet;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.Acessify.Callback.ImagenCallback;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterGaleriaSheet;
import com.example.Acessify.model.Local;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GaleriaSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GaleriaSheet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageSlider imageSlider;
    private Local local;
    private  ArrayList<SlideModel> slideModels = new ArrayList<>();
    private RecyclerView recyclerInfra;
    private AdapterGaleriaSheet adapterGaleria;


    public GaleriaSheet(Local local) {
        this.local = local;
    }

    public GaleriaSheet() {
        // Required empty public constructor

    } public static GaleriaSheet newInstance(Local local) {
        GaleriaSheet fragment = new GaleriaSheet();
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
     * @return A new instance of fragment GaleriaSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static GaleriaSheet newInstance(String param1, String param2) {
        GaleriaSheet fragment = new GaleriaSheet();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_galeria_sheet, container, false);
        imageSlider = view.findViewById(R.id.sliderGaleria);
        //regatar imagems
        resgatarImg(view);
        resgatarInfra(view);

        return view;

    }
    public void resgatarImg(View view){
        local.resgatarImagens(new ImagenCallback() {
            @Override
            public void onImagensCarregadas(List<Uri> imgs) {
                for (Uri img:imgs){
                    slideModels.add(new SlideModel(img.toString(),ScaleTypes.CENTER_CROP));
                    Log.d("onImagensCarregadas: ",img.toString());
                }
                imageSlider.setImageList(slideModels);
                imageSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        //aumentar imagem e pausar o carrossel;
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = requireActivity().getLayoutInflater();
                        View v = inflater.inflate(R.layout.alert_carrosel_sheet,null);


                        builder.setView(v);
                        //colocar img
                        imageSlider.stopSliding();
                        ImageView imageView = (ImageView) v.findViewById(R.id.alertCarroselImg);

                        Picasso.get().load(imgs.get(i)).into(imageView);
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                               imageSlider.startSliding(1000);
                            }
                        });

                        builder.create().show();


                    }
                });
            }
        });
    }
    public void resgatarInfra(View view){
        List<String[]> nivelInfra = local.resgatarAcesibilidae();
        if (nivelInfra.size( )!=0){
            recyclerInfra = view.findViewById(R.id.recyclerInfra);
            adapterGaleria = new AdapterGaleriaSheet(nivelInfra);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);

            recyclerInfra.setLayoutManager(layoutManager);
            recyclerInfra.setHasFixedSize(true);
            recyclerInfra.setNestedScrollingEnabled(false);
            recyclerInfra.setAdapter(adapterGaleria);
        }


    }
}