package com.example.Acessify.Callback;

import android.net.Uri;

import java.util.List;

public interface CorCallback {
    void onCorTrocada(int tipo, int cor);
    /* tipos:
    0 fechar
    1 pele
    2 cabelo
    3 oculos
    4 acessorio
    5 barba
     */
}
