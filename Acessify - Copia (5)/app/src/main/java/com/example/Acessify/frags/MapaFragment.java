package com.example.Acessify.frags;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Acessify.R;
import com.example.Acessify.adapter.AdapterMapaProximos;
import com.example.Acessify.adapter.AdapterPesquisa;
import com.example.Acessify.bottomSheet.MyBottomSheet;
import com.example.Acessify.helper.MeuMarker;
import com.example.Acessify.helper.RecyclerItemClickListener;
import com.example.Acessify.model.Local;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MapaFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference refLocal = Local.REF_LOCALIZACAO;
    private GeoFire geoFire = new GeoFire(refLocal);
    private ArrayList<String> idsProximos = new ArrayList<>();
    private ArrayList<Local> LocaisPesquisados = new ArrayList<>();
    private ArrayList<Local> locaisProximos = new ArrayList<>();
    private ArrayList<MeuMarker> locais = new ArrayList<>();
    Double lat, lon;
    GeoQuery geoQuery;
    Marker Muser, lclPesquisado;

    LatLng lnUser;
    //recycler
    RecyclerView recyclerMapa,pesquisaRecycler;
    AdapterPesquisa adapterPesquisa;
    AdapterMapaProximos adapterMapaProximos = null;
    GoogleMap map;
    Local local;
    RadioGroup radioMapa;

    Circle c;
    Integer areaBusca = 5000;
    ImageButton realocarBtn;
    LottieAnimationView loadIC;
    private Map<Marker, View> markerViews = new HashMap<>();

    private EditText barraPesquisa;
    ArrayList<String> ids = new ArrayList<>();
    private boolean isClose;
    // cache de BitmapDescriptor por chave (ex: idLocal + escala)
    private final Map<String, BitmapDescriptor> descriptorCache = new HashMap<>();
    // cache de ultima escala aplicada (para não redesenhar sem necessidade)
    private final Map<Marker, Float> markerLastScale = new HashMap<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            isClose = false;
            estanciarRadio();


            localizacao(googleMap);
            if (Muser != null) {
                Muser = googleMap.addMarker(new MarkerOptions().position(lnUser).title("Sua posição").icon(BitmapDescriptorFactory.fromResource(R.drawable.lnuser_icon)).zIndex(0));
                Muser.setPosition(lnUser);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lnUser, 15));
                c = map.addCircle(new CircleOptions().radius(areaBusca).center(lnUser).fillColor(ContextCompat.getColor(getContext(),R.color.circulo)).strokeColor(ContextCompat.getColor(getContext(),R.color.circulo_borda)));


            }
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    for (Local l : locaisProximos) {
                        if (l.getNomeLocal().equals(marker.getTitle())) {
                            local = l;
                            myBottom(local);
                        }
                    }

                    return false;
                }
            });
            realocarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realocarCamera();
                }
            });
            configurarCamera();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        isClose = false;
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        recyclerMapa = getView().findViewById(R.id.recyclerMapa);
        recyclerMapa.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerMapa.setHasFixedSize(true);
        recyclerClick();
        barraPesquisa = getView().findViewById(R.id.barraPesquisa);
        estanciarPesquisa();
        radioMapa = getView().findViewById(R.id.radioMapa);
        isClose = false;


        loadIC = getView().findViewById(R.id.loadIc);
        loadIC.setVisibility(View.GONE);
        pesquisaRecycler = getView().findViewById(R.id.recyclerPesquisa);
        adapterPesquisa = new AdapterPesquisa(LocaisPesquisados);
        pesquisaRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        pesquisaRecycler.setAdapter(adapterPesquisa);
        pesquisaRecycler.setVisibility(View.GONE);
        if (map != null){
            if (lnUser != null){

                idsProximos.clear();
                locaisProximos.clear();
                verificarLocais(lnUser.latitude,lnUser.longitude);
            }
            localizacao(map);
        }
        Log.d("reaberto", "view craida ");

        realocarBtn = getView().findViewById(R.id.realocarBtn);




    }




    public void localizacao(GoogleMap googleMap) {
        Log.d("mapa", "localizacao chamada");
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).setMinUpdateDistanceMeters(5).build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the use     r grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    lnUser = new LatLng(lat, lon);
                    if (Muser == null) {
                        Muser = googleMap.addMarker(new MarkerOptions().position(lnUser).title("Sua posição").icon(BitmapDescriptorFactory.fromResource(R.drawable.lnuser_icon)).zIndex(0));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lnUser, 15));


                    } else {
                        Muser.setPosition(lnUser);
                    }
                }
                }

        });
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null){
                    lat = locationResult.getLastLocation().getLatitude();
                    lon = locationResult.getLastLocation().getLongitude();
                    lnUser = new LatLng(lat, lon);
                    if (lnUser!= null){
                        if (Muser == null) {
                            Muser = googleMap.addMarker(new MarkerOptions().position(lnUser).title("Sua posição").icon(BitmapDescriptorFactory.fromResource(R.drawable.lnuser_icon)).zIndex(0));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lnUser, 15));


                        } else {
                            Muser.setPosition(lnUser);
                        }

                    }
                   //tirei aqui viu: locais.clear();


                    if (geoQuery == null) {
                        if (isClose){
                            return;
                        }
                        c = map.addCircle(new CircleOptions().radius(areaBusca).center(lnUser).fillColor(ContextCompat.getColor(getContext(),R.color.circulo)).strokeColor(ContextCompat.getColor(getContext(),R.color.circulo_borda)));

                        //verificar se a geoquery ja existe
                        verificarLocais(lat, lon);

                    } else {
                        if (isClose){
                            return;
                        }
                        ids.addAll(idsProximos);
                        if (geoQuery != null) {
                            geoQuery.setLocation(new GeoLocation(lat, lon), areaBusca/1000);
                        }


                        if (c != null){
                            c.setCenter(lnUser);
                        }else {
                            c = map.addCircle(new CircleOptions().radius(areaBusca).center(lnUser).fillColor(ContextCompat.getColor(getContext(),R.color.circulo)).strokeColor(ContextCompat.getColor(getContext(),R.color.circulo_borda)));
                        }






                    }
                }



            }
        }, Looper.getMainLooper());
    }

    private void verificarLocais(double lat, double lon) {
        if (isClose){
            return;
        }
        Log.d("mapa", "verificar lcoais");

        idsProximos.clear();
        locaisProximos.clear();




        geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lon), areaBusca / 1000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String s, GeoLocation geoLocation) {
                idsProximos.add(s);


            }

            @Override
            public void onKeyExited(String s) {

                idsProximos.remove(s);


            }

            @Override
            public void onKeyMoved(String s, GeoLocation geoLocation) {

            }

            @Override
            public void onGeoQueryReady() {
                //exibir Locais

                if (!new HashSet<>(idsProximos).equals(new HashSet<>(ids)))
                {
                    ids.clear();
                    resgatarLocais(idsProximos);
                }else {
                    Log.d("mapa", "locais iguais");
                    /*
                    ids.clear();
                    recyclerMapa.setVisibility(View.GONE);
                    removerMarkers();
                    verificarLocais(lat, lon);
                     */


                }

            }

            @Override
            public void onGeoQueryError(DatabaseError databaseError) {

            }
        });
    }

    public void resgatarLocais(ArrayList<String> l) {
        if (isClose){
            return;
        }
        Log.d("mapa", "resgartar locais");

        locaisProximos.clear();
        if (idsProximos.isEmpty()) {
            Log.d("mapa", "Vazio resgatar");
            recyclerMapa.setVisibility(View.GONE);
            removerMarkers();

        } else {
            recyclerMapa.setVisibility(View.VISIBLE);
            int cont[] = {0};
            for (String id : l) {
                Local.REF_LOCAL.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        locaisProximos.add(snapshot.getValue(Local.class));
                        cont[0]++;
                        if (cont[0] == l.size()) {
                            if (isClose){
                                return;
                            }
                            adapterMapaProximos = new AdapterMapaProximos(locaisProximos);
                            recyclerMapa.setAdapter(adapterMapaProximos);
                            recyclerMapa.setVisibility(View.VISIBLE);
                            adapterMapaProximos.notifyDataSetChanged();
                            adicionarMarcadores();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                    }
                });
            }

        }


    }

    public void recyclerClick() {
        recyclerMapa.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerMapa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                local = locaisProximos.get(position);
                myBottom(local);


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }

    //marcadores
    public void removerMarkers(){
        if (map == null) return;
        for (MeuMarker k : new ArrayList<>(locais)) {
            if (k == null) continue;
            Marker mrk = k.getMarker();
            if (mrk != null) {
                try { mrk.remove(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
        locais.clear();
        markerLastScale.clear();
        descriptorCache.clear(); // limpa cache se quiser liberar memória
    }
    public void removerTudo(){
        map.clear();

    }
    public void adicionarMarcadores() {
        if (isClose || map == null) return;

        // remove antigos markers da tela
        for (MeuMarker k : new ArrayList<>(locais)) {
            if (k != null && k.getMarker() != null) {
                try { k.getMarker().remove(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
        locais.clear();
        markerLastScale.clear();

        for (Local l : locaisProximos) {
            if (isClose || map == null) break;
            MeuMarker marker = gerarMarcador(l);
            if (marker == null) continue;
            LatLng ln = new LatLng(Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon()));
            BitmapDescriptor bd = gerarMarcadorView(1f, marker);
            if (bd == null) bd = BitmapDescriptorFactory.defaultMarker();
            Marker m = map.addMarker(new MarkerOptions().position(ln).icon(bd).title(l.getNomeLocal()));
            marker.setMarker(m);
            locais.add(marker);
        }
        tamanhoMarkers();
    }


    private MeuMarker gerarMarcador(Local l) {
        if (isClose){
            return null;
        }
        MeuMarker marker = new MeuMarker();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.marker_layout, null);
        marker.setLocal(l);
        TextView text = view.findViewById(R.id.markerTxt);
        text.setText(l.getNomeLocal().toString());
        text.setTextColor(l.resgatarCor(getContext()));
        ImageView icon = view.findViewById(R.id.icon);
        icon.setImageResource(l.resgatarIcon());


        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        marker.setViewMarker(view);



        return marker;
    } //serve para recuperar os locais e seus marcadores de uma vez
    private BitmapDescriptor gerarMarcadorView(float escala, MeuMarker marker) {
        // fallback rápido
        if (marker == null || marker.getLocal() == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        Local l = marker.getLocal();
        String chave = l.getIdLocal() + "_" + Math.round(escala * 100); // precisa existir getIdLocal() ou algo único; adapte

        // retorna do cache se existir
        if (descriptorCache.containsKey(chave)) {
            return descriptorCache.get(chave);
        }

        // inflar view do marker
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.marker_layout, null);
        TextView text = view.findViewById(R.id.markerTxt);
        ImageView icon = view.findViewById(R.id.icon);

        if (escala < 0.95f) {
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
            text.setText(l.getNomeLocal());
        }
        try {
            text.setTextColor(l.resgatarCor(requireContext()));
        } catch (Exception e) {
            // fallback color se der ruim
            text.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
        }

        // set icon com cuidado: resgatarIcon() deve devolver drawable
        int drawableId = l.resgatarIcon();
        if (drawableId != 0) {
            Bitmap bmpIcon = drawableToBitmap(drawableId);
            if (bmpIcon != null) {
                icon.setImageBitmap(bmpIcon);
                // não recycle
            } else {
                // fallback para recurso direto
                icon.setImageResource(drawableId);
            }
        }

        // medir / layout
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        if (w <= 0) w = 48;
        if (h <= 0) h = 48;

        // desenhar em bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // escala e ajuste
        int width = Math.max(1, (int) (bitmap.getWidth() * escala * 1.2f));
        int height = Math.max(1, (int) (bitmap.getHeight() * escala * 1.2f));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        // validação extra — se scaledBitmap for inválido, fallback
        if (scaledBitmap == null || scaledBitmap.getWidth() == 0 || scaledBitmap.getHeight() == 0) {
            // fallback para drawable pin amarelo (garanta que existe)
            Bitmap fallbackBmp = drawableToBitmap(R.drawable.pinamarelo);
            if (fallbackBmp != null) {
                BitmapDescriptor fd = BitmapDescriptorFactory.fromBitmap(fallbackBmp);
                descriptorCache.put(chave, fd);
                return fd;
            }
            return BitmapDescriptorFactory.defaultMarker();
        }

        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap);
        // cachear
        descriptorCache.put(chave, descriptor);

        return descriptor;
    }
    //abiri bottom view
    public void myBottom(Local l) {
        MyBottomSheet myBottomSheet = MyBottomSheet.newInstance(l);
        myBottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheet");
        LatLng lng = new LatLng(Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
    }
    //pesquisaa
    private void estanciarPesquisa() {

        barraPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadIC.setVisibility(View.VISIBLE);
                if (s.length() == 0){
                    loadIC.setVisibility(View.GONE);
                    pesquisaRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    loadIC.setVisibility(View.GONE);
                    pesquisaRecycler.setVisibility(View.GONE);
                    adapterPesquisa.limpar();
                }
                if (s.length()> 1){
                    pesquisarLocais(s.toString().toLowerCase());
                }

            }
        });
        barraPesquisa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    loadIC.setVisibility(View.GONE);
                    pesquisaRecycler.setVisibility(View.GONE);
                }
            }
        });

    }

    public void pesquisarLocais(String pesq){

        ArrayList<Local> Locaisresult = new ArrayList<>();

        DatabaseReference nomeLocal = Local.REF_LOCAL;
        Query query = nomeLocal.orderByChild("nomeLocalLower").startAt(pesq.trim().toLowerCase()) .endAt(pesq + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot s: snapshot.getChildren()){
                        Local l = s.getValue(Local.class);
                        Locaisresult.add(l);
                        pesquisaRecycler.setVisibility(View.VISIBLE);
                        loadIC.setVisibility(View.GONE);


                    }
                    if (barraPesquisa.getText().toString().isEmpty()){
                        adapterPesquisa.limpar();
                    }else {
                        LocaisPesquisados.clear();
                        LocaisPesquisados.addAll(Locaisresult);
                        adapterPesquisa.notifyDataSetChanged();
                    }





                }else {
                    LocaisPesquisados.clear();
                    adapterPesquisa.notifyDataSetChanged();
                    pesquisaRecycler.setVisibility(View.GONE);
                    loadIC.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pesquisaRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), pesquisaRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myBottomPesquisa(LocaisPesquisados.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }
    public void myBottomPesquisa(Local l) {

        MyBottomSheet myBottomSheet = MyBottomSheet.newInstance(l);
        myBottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheet");

        LatLng lng = new LatLng(Double.parseDouble(l.getLat()), Double.parseDouble(l.getLon()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
        if (lclPesquisado == null){
            lclPesquisado = map.addMarker(new MarkerOptions().position(lng).icon(gerarMarcadorView(1,gerarMarcador(l))).zIndex(2).title(l.getNomeLocal()));
        }else {
            lclPesquisado.setPosition(lng);
            lclPesquisado.setIcon(gerarMarcadorView(1,gerarMarcador(l)));
            lclPesquisado.setZIndex(2);
            lclPesquisado.setTitle(l.getNomeLocal());
        }



    }
    //configurar camera
    public void configurarCamera(){
        if (map == null) return;
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                tamanhoMarkers();
            }
        });
    }
    public void tamanhoMarkers(){
        if (map == null) return; // segurança
        Float zoom = map.getCameraPosition().zoom;
        for (MeuMarker m : locais) {
            if (isClose) return;
            Marker marker = m.getMarker();
            if (marker == null) continue;

            float scala = 1f;
            if (zoom < 16f) {
                scala = (zoom / 14f);
                if (scala < 0.5f) scala = 0.5f;
            }

            Float last = markerLastScale.get(marker);
            if (last != null && Math.abs(last - scala) < 0.01f) {
                // mesma escala, não altera
                continue;
            }

            // gera descriptor (cache interno evita recomputo custoso)
            BitmapDescriptor bd = gerarMarcadorView(scala, m);
            if (bd != null) {
                try {
                    marker.setIcon(bd);
                    markerLastScale.put(marker, scala);
                } catch (Exception e) {
                    e.printStackTrace();
                    // fallback silencioso
                }
            }
        }
    }
   private void realocarCamera() {
        if (lnUser != null){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lnUser, 15));

        }

    }
    //radio group
    private void estanciarRadio() {

            radioMapa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int novaA;
                    if (checkedId == R.id.r5) {
                        novaA = 5000;
                    } else if (checkedId == R.id.r10) {
                        novaA = 10000;
                    } else {
                        novaA = 15000;
                    }
                    if (novaA != areaBusca) {
                        areaBusca = novaA;
                        if (lat != null && lon != null && c != null && geoQuery!= null) {
                            atualizarArea();
                        }
                    }

                }

            });
        }

    public void atualizarArea(){

        if (lat != null && lon != null && c != null && geoQuery!= null){

            removerTudo();
            c = map.addCircle(new CircleOptions().radius(areaBusca).center(lnUser).fillColor(ContextCompat.getColor(getContext(),R.color.circulo)).strokeColor(ContextCompat.getColor(getContext(),R.color.circulo_borda)));

            Muser = map.addMarker(new MarkerOptions().position(lnUser).title("Sua posição").icon(BitmapDescriptorFactory.fromResource(R.drawable.lnuser_icon)).zIndex(0));
            Log.d("TAG", "atualizarArea: ");
            verificarLocais(lat,lon);
        }
    }
    private Bitmap drawableToBitmap(int drawableRes) {
        try {
            Drawable d = ContextCompat.getDrawable(requireContext(), drawableRes);
            if (d == null) return null;
            int w = d.getIntrinsicWidth() > 0 ? d.getIntrinsicWidth() : 48;
            int h = d.getIntrinsicHeight() > 0 ? d.getIntrinsicHeight() : 48;
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            d.draw(canvas);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isClose = true;

        if (geoQuery != null) {
            geoQuery.removeAllListeners();
        }
        if (descriptorCache != null) descriptorCache.clear();

        locaisProximos.clear();
        idsProximos.clear();
    }
}