package com.example.Acessify.helper;

import android.view.View;

import com.example.Acessify.model.Local;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

public class MeuMarker {
    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public View getViewMarker() {
        return viewMarker;
    }

    public void setViewMarker(View viewMarker) {
        this.viewMarker = viewMarker;
    }

    private Marker marker;
    private View viewMarker;

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    private Local local;
    private Map<Float, BitmapDescriptor> cachedIcons = new HashMap<>();

    public BitmapDescriptor getCachedIcon(float escala) {
        return cachedIcons.get(escala);
    }

    public void cacheIcon(float escala, BitmapDescriptor icon) {
        cachedIcons.put(escala, icon);
    }


}
