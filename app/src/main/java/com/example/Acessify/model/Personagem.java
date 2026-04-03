package com.example.Acessify.model;

import android.graphics.Color;
import android.util.Pair;

import java.util.Arrays;
import java.util.List;

public class Personagem {

    public static final List<Pair<Integer,Integer>> coresPeles = Arrays.asList(new Pair<>(Color.parseColor("#FFB08B"),Color.parseColor("#F5855A")),
            new Pair<>(Color.parseColor("#E58E66"),Color.parseColor("#C0724F")),
            new Pair<>(Color.parseColor("#865843"),Color.parseColor("#593C2F")));//1 e o fill 2 o contorno

    public static final List<Integer> coresFios = Arrays.asList(Color.parseColor("#272727"),Color.parseColor("#5D392B"),
            Color.parseColor("#8F8987"),Color.parseColor("#E8AF5F"),Color.parseColor("#E65F3D"));
    public static final List<Integer> coresOculos = Arrays.asList(Color.parseColor("#7E7E7E"),Color.parseColor("#FFC956"),
            Color.parseColor("#B22C2C"),Color.parseColor("#1C54E0"));
    public static final List<Integer> coresBrincos = Arrays.asList(Color.parseColor("#7E7E7E"),Color.parseColor("#FFC956"));



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
