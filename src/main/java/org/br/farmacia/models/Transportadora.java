package org.br.farmacia.models;

import java.util.ArrayList;

public class Transportadora {
    private int id;
    private String nome;
    private String contato;
    private Integer empresaId;
    private String telefone;
    private String regiao;


    public Transportadora(int id, String nome, String contato, String telefone, String regiao) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.telefone = telefone;
        this.regiao = regiao;
    }

    public Transportadora() {
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }




}
