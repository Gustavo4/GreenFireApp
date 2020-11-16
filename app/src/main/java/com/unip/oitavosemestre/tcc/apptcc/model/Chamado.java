package com.unip.oitavosemestre.tcc.apptcc.model;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;

public class Chamado {

    private String id;
    private String nomeUsuario;
    private String imagem;
    private String localizacao;
    private Situacao situacao;
    private String descricao;
    private DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();

    public Chamado() {
    }

    public boolean salvar(Chamado chamado){
        try {
            firebase.child("chamado").child( getId() ).push().setValue( chamado );

            return true;
        } catch (DatabaseException e){
            e.printStackTrace();

            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Chamado Incêndio Aberto" +
                "Aberto por:" + nomeUsuario + '\'' +
                ", Situação atual: " + situacao;
    }
}
