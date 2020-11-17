package com.unip.oitavosemestre.tcc.apptcc.model;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Chamado {

    private String id;
    private String nomeUsuario;
    private String imagem;
    private String localizacao;
    private Situacao situacao;
    private String descricao;
    private String key;
    private DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();

    public Chamado() {
    }

    public Chamado(String id, String nomeUsuario, String imagem, String localizacao, Situacao situacao, String descricao) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.imagem = imagem;
        this.localizacao = localizacao;
        this.situacao = situacao;
        this.descricao = descricao;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Chamado Incêndio Aberto" +
                "Aberto por:" + nomeUsuario + '\'' +
                ", Situação atual: " + situacao;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nomeUsuario", nomeUsuario);
        result.put("imagem", imagem);
        result.put("localizacao", localizacao);
        result.put("situacao", situacao);
        result.put("descricao", descricao);

        return result;
    }

}
