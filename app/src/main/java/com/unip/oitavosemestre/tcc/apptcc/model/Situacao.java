package com.unip.oitavosemestre.tcc.apptcc.model;

public enum Situacao {

    Neutralizado(1), Controlada(2), ForaDeControle(3);

    private final int valor;

    Situacao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
