package com.example.Acessify.model;

import android.graphics.Color;
import android.util.Pair;

public class Pesonagem {

    public static final Pair<Integer,Integer> pele1 = new Pair<>(Color.parseColor("#FFB08B"),Color.parseColor("#F5855A"));
    public static final Pair<Integer,Integer> pele2 = new Pair<>(Color.parseColor("#E58E66"),Color.parseColor("#C0724F"));
    public static final Pair<Integer,Integer> pele3 = new Pair<>(Color.parseColor("#865843"),Color.parseColor("#593C2F"));
    private String id,cabelo,corCabelo,corPele,sexo,condicao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCabelo() {
        return cabelo;
    }

    public void setCabelo(String cabelo) {
        this.cabelo = cabelo;
    }

    public String getCorCabelo() {
        return corCabelo;
    }

    public void setCorCabelo(String corCabelo) {
        this.corCabelo = corCabelo;
    }

    public String getCorPele() {
        return corPele;
    }

    public void setCorPele(String corPele) {
        this.corPele = corPele;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }
}
