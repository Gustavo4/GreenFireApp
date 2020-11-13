package com.unip.oitavosemestre.tcc.apptcc.model;

public enum Situacao {

    Neutralizado(0), Controlado(1), Perigoso(2);

    private final int valor;

    Situacao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
