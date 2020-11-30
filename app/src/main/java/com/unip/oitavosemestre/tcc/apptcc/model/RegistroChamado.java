package com.unip.oitavosemestre.tcc.apptcc.model;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

import static com.unip.oitavosemestre.tcc.apptcc.model.Chamado.Status.Aberto;

public class RegistroChamado {

    private String id;
    private String nomeUsuario;
    private String imagem;
    private String localizacao;
    private Situacao situacao;
    private String descricao;
    private String key;
    private String usuarioEncerramento;
    private String data;
    private DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();

    public RegistroChamado() {
    }

    public RegistroChamado(String id, String nomeUsuario, String imagem, String localizacao, Situacao situacao, String descricao, String key, String usuarioEncerramento, String data) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.imagem = imagem;
        this.localizacao = localizacao;
        this.situacao = situacao;
        this.descricao = descricao;
        this.key = key;
        this.usuarioEncerramento = usuarioEncerramento;
        this.data = data;
    }

    public boolean salvar(RegistroChamado registro){
        try {
            key = firebase.push().getKey();
            firebase.child("registroChamado").child(key).setValue( registro );
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUsuarioEncerramento() {
        return usuarioEncerramento;
    }

    public void setUsuarioEncerramento(String usuarioEncerramento) {
        this.usuarioEncerramento = usuarioEncerramento;
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
        result.put("key", key);
        result.put("usuarioEncerramento", usuarioEncerramento);

        return result;
    }



}
