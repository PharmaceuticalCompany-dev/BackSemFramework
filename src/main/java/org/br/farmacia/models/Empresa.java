package org.br.farmacia.models;

public class Empresa {
    private Integer id;
    private String nome;
    private double caixaTotal;

    public Empresa() {
    }

    public Empresa(Integer id, String nome, double caixaTotal) {
        this.id = id;
        this.nome = nome;
        this.caixaTotal = caixaTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getCaixaTotal() {
        return caixaTotal;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCaixaTotal(double caixaTotal) {
        this.caixaTotal = caixaTotal;
    }

    public String getNome() {
        return nome;
    }
}
