package org.br.farmacia.models;

import java.util.ArrayList;

public class Transportadora {
    private int id;
    private String nome;
    private ArrayList<String> locaisAtendimento = new ArrayList<>();
    private Integer empresaId;

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public Transportadora(int id, String nome, ArrayList<String> locaisAtendimento) {
        this.id = id;
        this.nome = nome;
        this.locaisAtendimento = new ArrayList<>(locaisAtendimento);
    }

    public Transportadora() {
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


    public ArrayList<String> getLocaisAtendimento() {
        return locaisAtendimento;
    }

    public void setLocaisAtendimento(ArrayList<String> locaisAtendimento) {
        this.locaisAtendimento = locaisAtendimento;
    }

    public String toString(){
        return "Nome: " + getNome() + "\nLocais: " + getLocaisAtendimento();
    }
}
