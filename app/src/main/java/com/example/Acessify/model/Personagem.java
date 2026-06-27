package com.example.Acessify.model;

import android.graphics.Color;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Personagem {

    public static final List<Pair<Integer,Integer>> corPeles = new ArrayList<>(Arrays.asList(new Pair<>(Color.parseColor("#FFB08B"),Color.parseColor("#F3936E")),
            new Pair<>(Color.parseColor("#E58E66"),Color.parseColor("#C0724F")),
            new Pair<>(Color.parseColor("#865843"),Color.parseColor("#593C2F")))); //first = preenchiment second = borda
    public static final Integer[] corFios = {Color.parseColor("#272727"),Color.parseColor("#5D392B"),Color.parseColor("#8F8987"),Color.parseColor("#E8AF5F"),Color.parseColor("#E65F3D")};
   public static final Integer[] corOculos ={Color.parseColor("#7E7E7E"),Color.parseColor("#FFC956"),Color.parseColor("#B22C2C"),Color.parseColor("#1C54E0"),Color.parseColor("#000000")};
    public static final Integer[] corAcessorio ={Color.parseColor("#7E7E7E"),Color.parseColor("#FFC956")};


    private String id;
    private String cabelo;
    private String corCabelo;
    private String corPele;
    private String sexo;
    private String condicao;
    private String boca;

    public String getBoca() {
        return boca;
    }

    public void setBoca(String boca) {
        this.boca = boca;
    }

    public String getNariz() {
        return nariz;
    }

    public void setNariz(String nariz) {
        this.nariz = nariz;
    }

    private String nariz;

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
